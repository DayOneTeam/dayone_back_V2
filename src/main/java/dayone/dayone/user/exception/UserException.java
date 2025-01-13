package dayone.dayone.user.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class UserException extends CommonException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
