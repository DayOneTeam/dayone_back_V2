package dayone.dayone.book.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookErrorCode implements ErrorCode {

    TITLE_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 1001, "책 제목은 null 이어거나 빈 값일 수 없습니다"),
    TITLE_LENGTH_OVER(HttpStatus.BAD_REQUEST, 1001, "제목은 100자를 넘길 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    BookErrorCode(final HttpStatus httpStatus, final int code, final String message) {
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
