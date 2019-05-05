package bthulu.commons.combine.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {
    /**
     * 缓存名
     */
    String value() default "";

    /**
     * 缓存key, 参考spel表达式, 默认为冒号分割的方法入参, 无入参则为空.
     * 最终的缓存key为<code>value:key</code>.
     */
    String key() default "";

    /**
     * 超时时间, 单位:秒, 默认60秒
     */
    int timeout() default 60;
}
