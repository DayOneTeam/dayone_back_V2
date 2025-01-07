package dayone.dayone.book.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class BookException extends CommonException {

    public BookException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
