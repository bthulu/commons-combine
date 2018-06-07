package pondcat.commons.combine;

/**
 * 错误码参考了阿里开放平台的公共错误码:<a href="https://docs.open.alipay.com/common/105806"></a>
 *
 * @author gejian
 */
public class Result<T> {

	public static final String CODE_SUCCESS = "10000"; // 接口调用成功

	public static final String CODE_NOT_AVAILABLE = "20000"; // 服务不可用

	public static final String CODE_UNAUTHORIZED = "20001"; // 授权权限不足

	public static final String CODE_LACK_ARGUMENT = "40001"; // 缺少必选参数

	public static final String CODE_ILLEGAL_ARGUMENT = "40002"; // 非法的参数

	public static final String CODE_SERVICE_FAILED = "40000"; // 业务处理失败

	private String code; // 返回码

	private String msg; // 返回码描述

	private String subCode; // 业务返回码

	private String subMsg; // 业务返回码描述

	private T data; // 正常调用返回结果

	/**
	 * used for deserializable
	 */
	private Result() {
	}

	private Result(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	private Result(String code, String msg, String subCode, String subMsg, T data) {
		this.code = code;
		this.msg = msg;
		this.subCode = subCode;
		this.subMsg = subMsg;
		this.data = data;
	}

	private static final Result<Void> OK = new Result<>(CODE_SUCCESS, null, null);

	public static Result<Void> ok() {
		return OK;
	}

	public static <T> Result<T> ok(T data) {
		return new Result<>(CODE_SUCCESS, null, data);
	}

	public static <T> Result<T> ok(T data, String msg, String subCode, String subMsg) {
		return new Result<>(CODE_SUCCESS, msg, subCode, subMsg, data);
	}

	public static Result<Void> error(String msg) {
		return new Result<>(CODE_SERVICE_FAILED, msg, null);
	}

	public static Result<Void> error(String code, String msg) {
		assertCodeNotBlank(code);
		return new Result<>(code, msg, null);
	}

	public static Result<Void> error(String code, String msg, String subCode, String subMsg) {
		assertCodeNotBlank(code);
		Result<Void> result = new Result<>(code, msg, null);
		result.subCode = subCode;
		result.subMsg = subMsg;
		return result;
	}

	private static void assertCodeNotBlank(String code) {
		if (code == null || code.isEmpty()) {
			throw new ResultException(CODE_SERVICE_FAILED, "return code can not be null or empty");
		}
	}

	/**
	 * get data without return code handled, any un-success will throw an {@link ResultException}
	 *
	 * @return data
	 */
	public T data() {
		// should never reached
		assertCodeNotBlank(code);
		if (CODE_SUCCESS.equals(code)) {
			return data;
		}
		throw new ResultException(code, msg, subCode, subMsg);
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static class ResultException extends RuntimeException {
		private final String code;

		private String subCode;

		private String subMsg;

		public ResultException(String code, String message) {
			super(message);
			this.code = code;
		}

		public ResultException(String code, String message, String subCode, String subMsg) {
			super(message);
			this.code = code;
			this.subCode = subCode;
			this.subMsg = subMsg;
		}

		public String getCode() {
			return code;
		}

		public String getSubCode() {
			return subCode;
		}

		public String getSubMsg() {
			return subMsg;
		}
	}
}
