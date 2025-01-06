package dayone.dayone.global.exception;

import dayone.dayone.global.response.CommonResponseDto;

public record ExceptionResponse(
    int code,
    String message
) {

    public static CommonResponseDto<ExceptionResponse> toResponse(final CommonException exception) {
        return CommonResponseDto.forFailure(exception.getCode(), exception.getMessage());
    }
}
