package bthulu.commons.combine.reflect;

import bthulu.commons.combine.collection.MapUtil;
import com.sun.beans.WeakCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 实现深度的BeanOfClasssA<->BeanOfClassB复制, 仅复制名字, 类型相同的字段. 不要是用Apache Common
 * BeanUtils进行类复制，每次就行反射查询对象的属性列表, 非常缓慢.
 */
public abstract class BeanUtil extends BeanUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    private static final class BeanCopierHolder {

        private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

        private static byte copierType = 0;

        static {
            try {
                Class.forName("org.springframework.cglib.beans.BeanCopier");
                copierType = 1;
            } catch (Throwable e) {
                try {
                    Class.forName("net.sf.cglib.beans.BeanCopier");
                    copierType = 2;
                } catch (Throwable ignored) {
                }
            }
        }

    }

    private static Object getBeanCopier(Class source, Class target) {
        String key = source.getName() + '-' + target.getCanonicalName();
        Object copier = BeanCopierHolder.CACHE.get(key);
        if (copier == null) {
            if (BeanCopierHolder.copierType == 1) {
                copier = org.springframework.cglib.beans.BeanCopier.create(source, target, false);
            } else if (BeanCopierHolder.copierType == 2) {
                copier = net.sf.cglib.beans.BeanCopier.create(source, target, false);
            } else {
                throw new NullPointerException("needs cglib or spring cglib");
            }
            Object o = BeanCopierHolder.CACHE.putIfAbsent(key, copier);
            if (o != null) {
                return o;
            }
        }
        return copier;
    }

    /**
     * 从source拷贝名称类型相同的属性至target对象并返回. 性能是apache
     * commons或spring的BeanUtils#copyProperties的30倍以上.
     *
     * @param source 源
     * @param target 目标对象
     * @return 目标对象, 属性同source
     */
    public static <S, T> T copy(S source, T target) {
        if (source == null) {
            return null;
        }
        if (BeanCopierHolder.copierType == 1) {
            org.springframework.cglib.beans.BeanCopier copier = (org.springframework.cglib.beans.BeanCopier) getBeanCopier(
                    source.getClass(), target.getClass());
            copier.copy(source, target, null);
        } else {
            net.sf.cglib.beans.BeanCopier copier = (net.sf.cglib.beans.BeanCopier) getBeanCopier(
                    source.getClass(), target.getClass());
            copier.copy(source, target, null);
        }
        return target;
    }

    /**
     * 从source拷贝名称类型相同的属性至supplier.get()对象并返回. 性能是apache
     * commons或spring的BeanUtils#copyProperties的30倍以上.
     *
     * @param source   源
     * @param supplier 目标提供者
     * @return supplier.get()对象, 属性同source
     */
    public static <S, T> T copy(S source, Supplier<T> supplier) {
        return copy(source, supplier.get());
    }

    /**
     * 批量拷贝名称类型相同的属性至supplier.get()对象并返回. 性能是apache
     * commons或spring的BeanUtils#copyProperties的30倍以上.
     *
     * @param sources  源集合
     * @param supplier 目标提供者
     * @return supplier.get()对象, 属性同source
     */
    public static <S, T> List<T> copy(Collection<S> sources, Supplier<T> supplier) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        for (S s : sources) {
            list.add(copy(s, supplier));
        }
        return list;
    }


    public static <S, T> T copyPropertiesNonNull(S source, T target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (value == null) {
                                continue;
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
        return target;
    }

    public static <S, T> T copyPropertiesNonNull(S source, Supplier<T> supplier) {
        return copyPropertiesNonNull(source, supplier.get());
    }


    private static final WeakCache<Class<?>, Field[]> fieldCache = new WeakCache<>();

    private static final Map<Class<?>, Object> defaultMap = new HashMap<>();

    static {
        defaultMap.put(Integer.class, 0);
        defaultMap.put(Long.class, 0L);
        defaultMap.put(Short.class, (short) 0);
        defaultMap.put(Byte.class, (byte) 0);
        defaultMap.put(Boolean.class, Boolean.FALSE);
        defaultMap.put(Float.class, 0.0F);
        defaultMap.put(Double.class, 0.0D);
        defaultMap.put(String.class, "");
    }

    /**
     * 初始化目标对象的包装类型(Integer, Long, Short, Byte, Boolean, Float, Double, String)字段
     *
     * @param target 被初始化对象
     */
    public static void writeDefaultValue(Object target) {
        writeDefaultValue(target, null);
    }

    /**
     * 初始化目标对象的包装类型(Integer, Long, Short, Byte, Boolean, Float, Double, String)字段,
     * 如果指定了某种类型的字段的默认值, 则也会初始化. 如果指定的字段类型与全局类型重复, 以指定字段类型的默认值为准.
     *
     * @param target   被初始化对象
     * @param defaults 指定的默认值
     */
    public static void writeDefaultValue(Object target, Map<Class<?>, Object> defaults) {
        if (target == null) {
            return;
        }

        Field[] fields = getFields(target);
        for (Field field : fields) {
            if (field == null) {
                continue;
            }
            Object v;
            try {
                v = field.get(target);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (v != null) {
                continue;
            }
            Class<?> type = field.getType();
            // 先取入参中指定的默认值
            if (defaults != null) {
                v = defaults.get(type);
            }
            // 入参中未指定默认值, 再从全局默认值里取
            if (v == null) {
                v = defaultMap.get(type);
            }
            if (v != null) {
                try {
                    field.set(target, v);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    private static Field[] getFields(Object o) {
        Class<?> aClass = o.getClass();
        synchronized (fieldCache) {
            Field[] result = fieldCache.get(aClass);
            if (result == null) {
                result = aClass.getDeclaredFields();
                for (int i = 0; i < result.length; i++) {
                    Field field = result[i];
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isTransient(modifiers) || Modifier.isNative(modifiers)) {
                        result[i] = null;
                    }
                }
                fieldCache.put(aClass, result);
            }
            return result;
        }
    }

    /**
     * 将pojo转化为map, 仅转化pojp的getter方法
     *
     * @param pojo java bean对象
     * @return map
     */
    public static Map<String, ?> toMap(Object pojo) {
        if (pojo instanceof Number || pojo instanceof String) {
            return Collections.emptyMap();
        }
        Method[] methods = pojo.getClass().getMethods();
        Map<String, Object> map = new HashMap<>(MapUtil.capacity(methods.length / 2 + 3));
        for (Method method : methods) {
            String name = method.getName();
            if (name.length() < 4 || !name.startsWith("get") || method.getParameterCount() > 0
                    || name.equals("getClass")) {
                continue;
            }
            try {
                map.put(name.substring(3, 4).toLowerCase() + name.substring(4), method.invoke(pojo));
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("method invoke error", e);
            }
        }
        return map;
    }
}
