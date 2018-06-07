package pondcat.commons.combine.exception;

/**
 * CheckedException的wrapper, 返回Message时, 将返回内层Exception的Message.
 *
 * 仅用于包装受检异常, 如可提供详细信息, 不要使用
 */
public class UncheckedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Throwable cause;

	public UncheckedException(Throwable wrapped) {
		super((Throwable) null);
		this.cause = wrapped;
	}

	@Override
	public String getMessage() {
		return cause.getMessage();
	}

	@Override
	public String getLocalizedMessage() {
		return cause.getLocalizedMessage();
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	@Override
	public String toString() {
		return cause.toString();
	}

}
