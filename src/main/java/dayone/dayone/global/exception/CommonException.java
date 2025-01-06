package dayone.dayone.global.exception;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommonException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public String getMessage() {
        return errorCode.getMessage();
    }
}
