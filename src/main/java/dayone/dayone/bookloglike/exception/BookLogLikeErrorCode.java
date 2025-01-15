package dayone.dayone.bookloglike.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookLogLikeErrorCode implements ErrorCode {

    ALREADY_LIKE_BOOK_LOG(HttpStatus.BAD_REQUEST, 3001, "이미 좋아요를 누른 bookLog 입니다."),
    NOT_LIKE_BOOK_LOG(HttpStatus.BAD_REQUEST, 3002, "좋아요를 누르지 않은 bookLog 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    BookLogLikeErrorCode(HttpStatus httpStatus, int code, String message) {
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
