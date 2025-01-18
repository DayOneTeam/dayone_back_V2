package dayone.dayone.user.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    EMAIL_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 2001, "이메일 정보는 null이거나 빈 값일 수 없습니다."),
    INVALID_EMAIL_FORM(HttpStatus.BAD_REQUEST, 2002, "올바른 이메일 형식이 아닙니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, 2003, "존재하지 않는 유저입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, int code, String message) {
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
