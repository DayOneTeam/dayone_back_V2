package dayone.dayone.auth.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class TokenException extends CommonException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
