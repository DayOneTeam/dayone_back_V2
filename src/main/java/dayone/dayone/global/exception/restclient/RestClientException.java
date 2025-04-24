package dayone.dayone.global.exception.restclient;

import dayone.dayone.global.exception.CommonException;
import dayone.dayone.global.exception.ErrorCode;

public class RestClientException extends CommonException {
    public RestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
