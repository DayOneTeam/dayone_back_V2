package dayone.dayone.global.exception;

import dayone.dayone.global.response.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ExceptionResponse(
    int code,
    String message
) {

    public static ResponseEntity<CommonResponseDto<ExceptionResponse>> toResponse(final CommonException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
            .body(CommonResponseDto.forFailure(exception.getCode(), exception.getMessage()));
    }

    public static ResponseEntity<CommonResponseDto<ExceptionResponse>> toResponse(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(CommonResponseDto.forFailure(exception.getMessage()));
    }
}
