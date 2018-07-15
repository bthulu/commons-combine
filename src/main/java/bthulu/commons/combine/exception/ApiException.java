package bthulu.commons.combine.exception;

public class ApiException extends RuntimeException {
    private final String status;

    private final String code;

	public ApiException(String status, String message) {
		this(status, message, null);
    }

	public ApiException(String status, String message, String code) {
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
