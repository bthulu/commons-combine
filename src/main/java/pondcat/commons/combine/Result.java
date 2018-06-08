package pondcat.commons.combine;

/**
 * 返回数据包装类, 其中返回码参考了http status的定义.
 * 建议继承此类, 重载{@link #ok(Object)}和{@link #ok(Object, String)}以支持分页
 */
public class Result<T> {

	public static final String STATUS_SUCCESS = "200"; // 接口调用成功
	public static final String STATUS_SERVICE_FAILED = "400"; // 业务处理失败
	public static final String STATUS_UNAUTHORIZED = "401"; // 授权权限不足
	public static final String STATUS_FORBIDDEN = "403"; // 拒绝执行请求
	public static final String STATUS_NOT_FOUND = "404"; // 请求资源无法找到
	public static final String STATUS_SERVICE_UNAVAILABLE = "503"; // 服务不可用
	public static final String STATUS_LACK_ARGUMENT = "4001"; // 缺少必选参数
	public static final String STATUS_ILLEGAL_ARGUMENT = "4002"; // 非法的参数

	private String status; // 业务处理状态

	private String code; // 业务返回码

	private T data; // 正常调用返回结果

	private String msg; // 额外的描述信息

	private Result() {
	}

	private Result(String status) {
		this.status = status;
	}

	public static Result<Void> ok() {
		return ImmutableResult.OK;
	}

	public static <T> Result<T> ok(T data) {
		return ok(data, null);
	}

	public static <T> Result<T> ok(T data, String msg) {
		Result<T> r = new Result<>(STATUS_SUCCESS);
		r.setData(data);
		r.setMsg(msg);
		return r;
	}

	public static Result<Void> error(String msg) {
		return error(STATUS_SERVICE_FAILED, msg);
	}

	public static Result<Void> error(String status, String msg) {
		Result<Void> r = new Result<>(status);
		r.setMsg(msg);
		return r;
	}

	public Result<T> code(String code) {
		setCode(code);
		return this;
	}

	/**
	 * get data without return code handled, any un-success will throw an {@link ResultException}
	 *
	 * @return data
	 */
	public T data() {
		if (STATUS_SUCCESS.equals(status)) {
			return data;
		}
		throw new ResultException(status, msg, code);
	}

	public static class ImmutableResult<T> extends Result<T> {
		private static final Result<Void> OK = new ImmutableResult<>();

		static {
			OK.status = STATUS_SUCCESS;
		}

		public ImmutableResult() {
			super();
		}

		@Override
		public void setStatus(String status) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCode(String code) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setData(T data) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMsg(String msg) {
			throw new UnsupportedOperationException();
		}

	}

	public static class ResultException extends RuntimeException {
		private final String status;

		private String code;

		public ResultException(String status, String message, String code) {
			super(message);
			this.status = status;
			this.code = code;
		}

		public String getStatus() {
			return status;
		}

		public String getCode() {
			return code;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Result{" + "status='" + status + '\'' + ", code='" + code + '\'' + ", data=" + data + ", msg='" + msg
				+ '\'' + '}';
	}
}
