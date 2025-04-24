package dayone.dayone.global.exception.restclient;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum RestClientErrorCode implements ErrorCode {

    CONNECTION_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR, 10001, "연결 시간이 초과되었습니다."),
    READ_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR, 10002, "읽기 시간이 초과되었습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 10003, "알 수 없는 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    RestClientErrorCode(final HttpStatus httpStatus, final int code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
