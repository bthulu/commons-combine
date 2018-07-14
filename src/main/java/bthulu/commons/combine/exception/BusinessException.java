package bthulu.commons.combine.exception;

/**
 * 业务异常, 仅标记异常为业务代码异常, 非第三方jar包内运行时异常. 业务异常禁止提供错误码的功能, 如需此功能, 请封装错误码到Pair或Triple返回.
 * 如果异常频繁发生，且不需要打印完整的调用栈时，可以考虑绕过异常的构造函数: 1. 如异常无需变化,
 * 定义静态异常{@link ExceptionUtil#setStackTrace(Throwable, Class, String)} 2. 如异常的message会变化,
 * 定义可克隆静态异常{@link CloneableException}, 后续调用{@link CloneableException#clone(String)}抛出
 */
public class BusinessException extends RuntimeException {

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
