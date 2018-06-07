package pondcat.commons.combine.transaction;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 事物回滚. 受检异常, 非受检异常, 错误均触发回滚
 * @see Transactional
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional(rollbackFor = Throwable.class)
public @interface Transaction {
}
