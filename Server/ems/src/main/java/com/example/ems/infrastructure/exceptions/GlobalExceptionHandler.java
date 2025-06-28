package com.example.ems.infrastructure.exceptions;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ApiCommonResponse<Object> handleUserException(UserException ex) {
        return ApiCommonResponse.create(
                HttpStatus.BAD_REQUEST,
                400,
                ex.getMessage(),
                null
        ).getBody();
    }
}
