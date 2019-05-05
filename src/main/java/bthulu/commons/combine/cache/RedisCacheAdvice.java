package bthulu.commons.combine.cache;

import bthulu.commons.combine.Pair;
import bthulu.commons.combine.exception.Asserts;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public abstract class RedisCacheAdvice extends StaticMethodMatcherPointcut implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheAdvice.class);
    private static final long maxTimeout = 3600 * 24 * 7;

    private final String cacheKeyPrefix;

    public RedisCacheAdvice(String prefix) {
        Asserts.hasText(prefix, "cache key prefix can not be empty");
        this.cacheKeyPrefix = "ca:" + prefix;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Method method = mi.getMethod();
        Cacheable cacheable = method.getDeclaredAnnotation(Cacheable.class);
        if (cacheable != null) {
            return cache(mi, cacheable);
        }
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
        if (cacheEvict != null) {
            return evict(mi, cacheEvict);
        }

        return mi.proceed();
    }

    private Object cache(MethodInvocation mi, Cacheable cacheable) throws Throwable {
        // 生成缓存key及timeout
        String key = null;
        long timeout = 60;
        // 从缓存获取数据
        try {
            // 生成缓存key及timeout
            Pair<String, Integer> pair = getCacheKeyAndTimeout(mi, cacheable);
            key = pair.getK();
            timeout = pair.getV();

            // 读取缓存
            String s = get(key);

            // 缓存命中, 返回缓存数据
            if (s != null) {
                if (log.isDebugEnabled()) {
                    log.debug("缓存命中:" + key);
                }
                Class<?> returnType = mi.getMethod().getReturnType();
                return returnType == void.class ? null : parseObject(s, returnType);
            }
        } catch (Throwable t) {
            log.warn("查询缓存出错", t);
        }

        // 缓存未命中, 执行切面方法
        if (log.isDebugEnabled()) {
            log.debug("缓存未命中:" + key);
        }
        Object proceed = mi.proceed();

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

    private Object evict(MethodInvocation mi, CacheEvict cacheEvict) throws Throwable {
        // 执行切面方法
        Object proceed = mi.proceed();

        // 删除缓存
        String key = "";
        try {
            key = getEvictKey(mi, cacheEvict);
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

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return method.getDeclaredAnnotation(Cacheable.class) != null || method.getDeclaredAnnotation(CacheEvict.class) != null;
    }

    private static final DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
    // 使用SPEL进行key的解析
    private ExpressionParser parser = new SpelExpressionParser();

    private Pair<String, Integer> getCacheKeyAndTimeout(MethodInvocation mi, Cacheable cacheable) {
        String cacheName = cacheable.value();
        String key = cacheable.key();
        String cacheKey = cacheKeyPrefix + processKey(mi, cacheName, key);
        return Pair.of(cacheKey, cacheable.timeout(), false);
    }

    private String getEvictKey(MethodInvocation mi, CacheEvict evict) {
        String cacheName = evict.value();
        String key = evict.key();
        return cacheKeyPrefix + processKey(mi, cacheName, key);
    }

    private String processKey(MethodInvocation mi, String cacheName, String key) {
        StringBuilder cacheKey = new StringBuilder(cacheName);
        Method method = mi.getMethod();
        if (cacheKey.length() == 0) {
            cacheKey = new StringBuilder(method.getClass().getSimpleName());
            cacheKey.append(":").append(method.getName());
        }
        Object[] args = mi.getArguments();
        if (args == null || args.length == 0) {
            return cacheKey.toString();
        }
        if (key.isEmpty()) {
            for (Object arg : args) {
                cacheKey.append(":").append(arg);
            }
            return cacheKey.toString();
        }
        // SPEL上下文
        String[] parameterNames = discover.getParameterNames(method);
        if (parameterNames != null && parameterNames.length == args.length) {
            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
            Object value = parser.parseExpression(key).getValue(context);
            cacheKey.append(":").append(value);
        }
        return cacheKey.toString();
    }

    abstract String get(String key);

    abstract void setex(String key, long seconds, String value);

    abstract void del(String key);

    abstract <T> T parseObject(String json, Class<T> target);

    abstract String toJSONString(Object object);
}
