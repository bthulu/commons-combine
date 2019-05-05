package bthulu.commons.combine.cache;

import bthulu.commons.combine.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public abstract class RedisCacheAdvice {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheAdvice.class);
    private static final long maxTimeout = 3600 * 24 * 7;
    // 使用SPEL进行key的解析
    private ExpressionParser parser = new SpelExpressionParser();

    @Pointcut("@annotation(bthulu.commons.combine.cache.Cacheable)")
    public void cacheCut() {
    }

    @Pointcut("@annotation(bthulu.commons.combine.cache.CacheEvict)")
    public void cacheEvictCut() {
    }

    private final String cacheKeyPrefix;

    public RedisCacheAdvice(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix + ":ca:";
    }

    @Around("cacheCut()")
    public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
        // 生成缓存key及timeout
        String key = null;
        long timeout = 60;

        // 从缓存获取数据
        try {
            // 生成缓存key及timeout
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Pair<String, Integer> pair = getCacheKeyAndTimeout(signature, joinPoint);
            key = pair.getK();
            timeout = pair.getV();

            // 读取缓存
            String s = get(key);

            // 缓存命中, 返回缓存数据
            if (s != null) {
                if (log.isDebugEnabled()) {
                    log.debug("缓存命中:" + key);
                }
                Class<?> returnType = signature.getReturnType();
                return returnType == void.class ? null : parseObject(s, returnType);
            }
        } catch (Throwable t) {
            log.warn("查询缓存出错", t);
        }

        // 缓存未命中, 执行切面方法
        if (log.isDebugEnabled()) {
            log.debug("缓存未命中:" + key);
        }
        Object proceed = joinPoint.proceed();

        // 写入缓存
        try {
            if (key != null) {
                setex(key, timeout > 0 ? timeout : maxTimeout, toJSONString(proceed));
                if (log.isDebugEnabled()) {
                    log.debug("写入缓存:" + key);
                }
            }
        } catch (Throwable t) {
            log.warn("写入缓存出错", t);
        }

        return proceed;
    }

    @Around("cacheEvictCut()")
    public Object doEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行切面方法
        Object proceed = joinPoint.proceed();

        // 删除缓存
        String key = "";
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            key = getEvictKey(signature, joinPoint);
            if (!key.isEmpty()) {
                del(key);
                if (log.isDebugEnabled()) {
                    log.debug("删除缓存:" + key);
                }
            }
        } catch (Throwable t) {
            log.warn("删除缓存:{}异常", key, t);
        }

        return proceed;
    }

    private Pair<String, Integer> getCacheKeyAndTimeout(MethodSignature signature, ProceedingJoinPoint joinPoint) {
        Method method = signature.getMethod();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        String cacheName = cacheable.value();
        String key = cacheable.key();
        String cacheKey = cacheKeyPrefix + processKey(signature, joinPoint, method, cacheName, key);
        return Pair.of(cacheKey, cacheable.timeout(), false);
    }

    private String getEvictKey(MethodSignature signature, ProceedingJoinPoint joinPoint) {
        Method method = signature.getMethod();
        CacheEvict evict = method.getAnnotation(CacheEvict.class);
        String cacheName = evict.value();
        String key = evict.key();
        return cacheKeyPrefix + processKey(signature, joinPoint, method, cacheName, key);
    }

    private String processKey(MethodSignature signature, ProceedingJoinPoint joinPoint, Method method, String cacheName, String key) {
        StringBuilder cacheKey = new StringBuilder(cacheName);
        if (cacheKey.length() == 0) {
            cacheKey = new StringBuilder(joinPoint.getTarget().getClass().getSimpleName());
            cacheKey.append(":").append(method.getName());
        }
        Object[] args = joinPoint.getArgs();
        if (key.isEmpty()) {
            for (Object arg : args) {
                cacheKey.append(":").append(arg);
            }
        } else {
            // SPEL上下文
            StandardEvaluationContext context = new StandardEvaluationContext();
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            Object value = parser.parseExpression(key).getValue(context);
            cacheKey.append(":").append(value);
        }
        return cacheKey.toString();
    }

    protected abstract String get(String key);

    protected abstract void setex(String key, long seconds, String value);

    protected abstract void del(String key);

    protected abstract <T> T parseObject(String json, Class<T> target);

    protected abstract String toJSONString(Object object);
}
