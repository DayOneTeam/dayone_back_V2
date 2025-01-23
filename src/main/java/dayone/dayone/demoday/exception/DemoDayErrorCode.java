package dayone.dayone.demoday.exception;

import dayone.dayone.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DemoDayErrorCode implements ErrorCode {

    DEMO_DAY_REGISTRATION_PERIOD_ERROR(HttpStatus.BAD_REQUEST, 5001, "신청 마감 시간이 신청 시작 시간보다 빠를 수 없습니다."),
    DEMO_DAY_IS_NOT_BEFORE_NOW(HttpStatus.BAD_REQUEST, 5002, "데모데이 날이 지금보다 이전일 수 없습니다."),
    DEMO_DAY_CAPACITY_UNDER_ZERO(HttpStatus.BAD_REQUEST, 5003, "데모데이 참여 인원은 0명이거나 음수가 될 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    DemoDayErrorCode(final HttpStatus httpStatus, final int code, final String message) {
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
