package dayone.dayone.auth.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum TokenErrorCode implements ErrorCode {

    NOT_ISSUED_TOKEN(HttpStatus.BAD_REQUEST, 3001, "서비스 내에서 발급한 토큰이 아닙니다"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, 3002, "만료된 토큰 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    TokenErrorCode(final HttpStatus httpStatus, final int code, final String message) {
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
