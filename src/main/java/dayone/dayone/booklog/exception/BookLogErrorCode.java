package dayone.dayone.booklog.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookLogErrorCode implements ErrorCode {

    COMMENT_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 1, "자신의 생각은 비어있어나 null 일 수 없습니다."),
    COMMENT_LENGTH_OVER(HttpStatus.BAD_REQUEST, 2, "책에 대한 자신의 생각은 5000자를 넘길 수 없습니다."),
    PASSAGE_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 3, "구절은 비어있어나 null 일 수 없습니다."),
    PASSAGE_LENGTH_OVER(HttpStatus.BAD_REQUEST, 4, "구절은 1000자를 넘길 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    BookLogErrorCode(final HttpStatus httpStatus, final int code, String message) {
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
