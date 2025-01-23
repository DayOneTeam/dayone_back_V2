package dayone.dayone.demoday.exception;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class DemoDayException extends CommonException {

    public DemoDayException(ErrorCode errorCode) {
        super(errorCode);
    }
}
