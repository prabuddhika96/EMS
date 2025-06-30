package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExecutionCode implements ExecutionCode{
    USER_NOT_FOUND(2001, "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(2002, "USER_ALREADY_EXISTS", HttpStatus.CONFLICT),
    USER_REGISTRATION_FAILED(2003, "USER_REGISTRATION_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_LIST_FOUND(2004, "USER_LIST_FOUND", HttpStatus.OK),
    USER_LIST_EMPTY(2005, "USER_LIST_EMPTY", HttpStatus.OK),
    USER_ROLE_CHANGED_SUCCESS(2006, "USER_ROLE_CHANGED_SUCCESS", HttpStatus.OK),
    INVALID_ROLE(2007, "INVALID_ROLE", HttpStatus.BAD_REQUEST),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}
