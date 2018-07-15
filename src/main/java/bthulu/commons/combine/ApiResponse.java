package bthulu.commons.combine;

import bthulu.commons.combine.exception.ApiException;

/**
 * 返回数据包装类, 其中返回码参考了http status的定义. 建议继承此类,
 * 重载{@link #ok(Object)}和{@link #ok(Object, String)}以支持分页
 */
public class ApiResponse<T> {

	public static abstract class Status {
		public static final String SUCCESS = "200"; // 接口调用成功

		public static final String SERVICE_FAILED = "400"; // 业务处理失败

		public static final String UNAUTHORIZED = "401"; // 授权权限不足

		public static final String FORBIDDEN = "403"; // 拒绝执行请求

		public static final String NOT_FOUND = "404"; // 请求资源无法找到

		public static final String SERVICE_UNAVAILABLE = "503"; // 服务不可用

		public static final String LACK_ARGUMENT = "4001"; // 缺少必选参数

		public static final String ILLEGAL_ARGUMENT = "4002"; // 非法的参数

	}

	private String status; // 业务处理状态

	private String code; // 业务返回码

	private T data; // 正常调用返回结果

	private String msg; // 额外的描述信息

	private ApiResponse() {
	}

	private ApiResponse(String status) {
		this.status = status;
	}

	public static ApiResponse<Void> ok() {
		return ImmutableApiResponse.OK;
	}

	public static <T> ApiResponse<T> ok(T data) {
		return ok(data, null);
	}

	public static <T> ApiResponse<T> ok(T data, String msg) {
		ApiResponse<T> r = new ApiResponse<>(Status.SUCCESS);
		r.setData(data);
		r.setMsg(msg);
		return r;
	}

	public static ApiResponse<Void> error(String msg) {
		return error(Status.SERVICE_FAILED, msg);
	}

	public static ApiResponse<Void> error(String status, String msg) {
		ApiResponse<Void> r = new ApiResponse<>(status);
		r.setMsg(msg);
		return r;
	}

	public ApiResponse<T> code(String code) {
		setCode(code);
		return this;
	}

	public static class ImmutableApiResponse<T> extends ApiResponse<T> {

		private static final ApiResponse<Void> OK = new ImmutableApiResponse<>();

		static {
			OK.status = Status.SUCCESS;
		}

		public ImmutableApiResponse() {
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

	public boolean isOk() {
		return Status.SUCCESS.equals(status);
	}

	public T data() {
		if (isOk()) {
			return getData();
		}
		throw new ApiException(status, msg, code);
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
		return "ApiResponse{" + "status='" + status + '\'' + ", code='" + code + '\'' + ", data=" + data + ", msg='" + msg
				+ '\'' + '}';
	}
}
