package dayone.dayone.book.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum BookErrorCode implements ErrorCode {

    TITLE_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 1001, "책 제목은 null 이어거나 빈 값일 수 없습니다"),
    TITLE_LENGTH_OVER(HttpStatus.BAD_REQUEST, 1002, "책 제목은 100자를 넘길 수 없습니다."),
    AUTHOR_BLACK_AND_NULL(HttpStatus.BAD_REQUEST, 1003, "책 작가명은 null 이어거나 빈 값일 수 없습니다"),
    AUTHOR_LENGTH_OVER(HttpStatus.BAD_REQUEST, 1004, "책 작가명은 100자를 넘길 수 없습니다."),
    PUBLISHER_BLANK_AND_NULL(HttpStatus.BAD_REQUEST, 1005, "책 출판사명은 null 이어거나 빈 값일 수 없습니다."),
    PUBLISHER_LENGTH_OVER(HttpStatus.BAD_REQUEST, 1006, "책 출판사명은 100자를 넘길 수 없습니다."),
    BOOK_NOT_EXIST(HttpStatus.BAD_REQUEST, 1007, "존재하지 않는 책입니다.");

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
