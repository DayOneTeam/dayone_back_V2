package dayone.dayone.booklog.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class BookLogException extends CommonException {

    public BookLogException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
