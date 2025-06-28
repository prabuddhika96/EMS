package com.example.ems.infrastructure.exceptions;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.infrastructure.constant.executioncode.CommonExecutionCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ApiCommonResponse<Object> handleUserException(UserException ex) {
        return ApiCommonResponse.create(
                ex.getExecutionCode(),
                null
        ).getBody();
    }

    @ExceptionHandler(CommonException.class)
    public ApiCommonResponse<Object> handleCommonException(CommonException ex) {
        return ApiCommonResponse.create(
                ex.getExecutionCode(),
                null
        ).getBody();
    }

    @ExceptionHandler(Exception.class)
    public ApiCommonResponse<Object> handleException(Exception ex) {
        return ApiCommonResponse.create(
                CommonExecutionCode.SOMETHING_WENT_WRONG,
                null
        ).getBody();
    }
}
