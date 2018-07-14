package bthulu.commons.combine.exception;

import bthulu.commons.combine.io.StringBuilderWriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 关于异常的工具类.
 *
 * 1. Checked/Uncheked及Wrap(如ExecutionException)的转换.
 *
 * 2. 打印Exception的辅助函数. (其中一些来自Common Lang ExceptionUtils)
 *
 * 3. 查找Cause(其中一些来自Guava Throwables)
 *
 * 4. StackTrace性能优化相关，尽量使用静态异常避免异常生成时获取StackTrace(Netty)
 *
 * @see CloneableException
 */
public class ExceptionUtil {

	private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

	/**
	 * 将CheckedException包装为unchecked异常抛出, 减少函数签名中的CheckExcetpion定义.
	 * @param ex to cast
	 * @return {@link UncheckedException}
	 */
	@SuppressWarnings("unchecked")
	public static RuntimeException unchecked(Throwable ex) {
		throw new UncheckedException(ex);
	}

	/**
	 * 将CheckedException泛型伪装直接抛出, 减少函数签名中的CheckExcetpion定义. 禁止在底层库中使用, 这里没有对入参异常做任何处理直接抛出,
	 * 如入参为受检异常, 抛出的依然是这个受检异常, 只是编译器不再提示要求处理. 如在spring事物中调用此方法, 因spring事物默认不回滚受检异常,
	 * 很可能造成误解, 如需使用, 请务必确保受检异常时触发事物回滚, 如:
	 * <code>@Transactional(rollbackFor=Throwable.class)</code>或者<code>@Transaction</code>
	 * @param ex to cast
	 * @param <T> the type of the Throwable
	 * @return 异常直接抛出, 无返回值
	 * @throws T the throwable as an unchecked throwable
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unsafe(Throwable ex) throws T {
		throw (T) ex; // rely on vacuous cast
	}

	/**
	 * 如果是著名的包裹类，从cause中获得真正异常. 其他异常则不变.
	 *
	 * Future中使用的ExecutionException 与 反射时定义的InvocationTargetException， 真正的异常都封装在Cause中
	 *
	 * 前面 unchecked() 使用的UncheckedException同理.
	 */
	public static Throwable unwrap(@Nonnull Throwable t) {
		if (t instanceof UncheckedException
				|| t instanceof java.util.concurrent.ExecutionException
				|| t instanceof java.lang.reflect.InvocationTargetException
				|| t instanceof UndeclaredThrowableException) {
			return t.getCause();
		}

		return t;
	}

	////// 输出内容相关 //////

	/**
	 * 将StackTrace[]转换为String, 供Logger或e.printStackTrace()外的其他地方使用.
	 *
	 * 为了使用StringBuilderWriter，没有用Throwables#getStackTraceAsString(Throwable)
	 */
	public static String getStackTrace(@Nonnull Throwable t) {
		StringBuilderWriter stringWriter = new StringBuilderWriter();
		t.printStackTrace(new PrintWriter(stringWriter)); // NOSONAR
		return stringWriter.toString();
	}

	////////// Cause 相关 /////////

	/**
	 * 获取某种类型的cause，如果没有则返回空
	 *
	 * copy from Jodd ExceptionUtil
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> T findCause(@Nonnull Throwable throwable,
			Class<T> cause) {
		while (throwable != null) {
			if (throwable.getClass().equals(cause)) {
				return (T) throwable;
			}
			throwable = throwable.getCause();
		}
		return null;
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(@Nullable Throwable throwable,
			Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = throwable;

		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
	/////////// StackTrace 性能优化相关////////

	/**
	 * copy from Netty, 为静态异常设置StackTrace.
	 *
	 * 对某些已知且经常抛出的异常, 不需要每次创建异常类并很消耗性能的并生成完整的StackTrace. 此时可使用静态声明的异常.
	 *
	 * 如果异常可能在多个地方抛出，使用本函数设置抛出的类名和方法名.
	 *
	 * <pre>
	 * private static RuntimeException TIMEOUT_EXCEPTION = ExceptionUtil.setStackTrace(new RuntimeException("Timeout"),
	 * 		MyClass.class, "mymethod");
	 * </pre>
	 */
	public static <T extends Throwable> T setStackTrace(@Nonnull T throwable,
			Class<?> throwClass, String throwClazz) {
		throwable.setStackTrace(new StackTraceElement[] {
				new StackTraceElement(throwClass.getName(), throwClazz, null, -1) });
		return throwable;
	}

	/**
	 * 清除StackTrace. 假设StackTrace已生成, 但把它打印出来也有不小的消耗.
	 *
	 * 如果不能控制StackTrace的生成，也不能控制它的打印端(如logger)，可用此方法暴力清除Trace.
	 *
	 * 但Cause链依然不能清除, 只能清除每一个Cause的StackTrace.
	 */
	public static <T extends Throwable> T clearStackTrace(@Nonnull T throwable) {
		Throwable cause = throwable;
		while (cause != null) {
			cause.setStackTrace(EMPTY_STACK_TRACE);
			cause = cause.getCause();
		}
		return throwable;
	}

}
