package bthulu.commons.combine.exception;

public class ApiException extends RuntimeException {
    private final String status;

    private final String code;

    private final String responseBody;

    public ApiException(String status, String message, String responseBody) {
        this(status, message, responseBody, null);
    }

    public ApiException(String status, String message, String responseBody, String code) {
        super(message);
        this.status = status;
        this.code = code;
        this.responseBody = responseBody;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
