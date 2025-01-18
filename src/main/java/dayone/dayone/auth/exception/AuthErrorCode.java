package dayone.dayone.auth.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {

    FAIL_LOGIN(HttpStatus.BAD_REQUEST, 4001, "이메일 혹은 비밀번호가 틀렸습니다."),
    HAVE_NOT_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, 4002, "refresh 토큰이 존재하지 않습니다."),
    NOT_LOGIN_USER(HttpStatus.UNAUTHORIZED, 4003, "로그인되지 않은 유저입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, int code, String message) {
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
