package dayone.dayone.global.exception;

import dayone.dayone.global.response.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponseDto<ExceptionResponse>> handleException(final CommonException exception) {
        return ResponseEntity.status(exception.getHttpStatus().value())
            .body(ExceptionResponse.toResponse(exception));
    }
}
