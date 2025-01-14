package dayone.dayone.bookloglike.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class BookLogLikeException extends CommonException {

    public BookLogLikeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
