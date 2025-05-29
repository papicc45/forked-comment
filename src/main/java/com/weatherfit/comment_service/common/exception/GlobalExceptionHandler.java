package com.weatherfit.comment_service.common.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.weatherfit.comment_service.common.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        ErrorResponse body = new ErrorResponse(ec.getCode(), ec.getMessage());
        return ResponseEntity
                .status(determineHttpStatus(ec))
                .body(body);
    }

    private HttpStatus determineHttpStatus(ErrorCode ec) {
        if(ec.name().startsWith("AUTH_")) return HttpStatus.CONFLICT;

        return HttpStatus.BAD_REQUEST;
    }
}
