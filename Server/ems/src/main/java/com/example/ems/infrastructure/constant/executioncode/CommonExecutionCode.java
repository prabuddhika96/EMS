package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExecutionCode implements ExecutionCode{
    SOMETHING_WENT_WRONG(5000, "SOMETHING_WENT_WRONG", HttpStatus.INTERNAL_SERVER_ERROR),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
