package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExecutionCode implements ExecutionCode{
    USER_NOT_FOUND(2001, "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(2002, "USER_ALREADY_EXISTS", HttpStatus.CONFLICT),
    USER_REGISTRATION_FAILED(2003, "USER_REGISTRATION_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}
