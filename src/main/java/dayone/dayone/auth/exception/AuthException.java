package dayone.dayone.auth.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class AuthException extends CommonException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
