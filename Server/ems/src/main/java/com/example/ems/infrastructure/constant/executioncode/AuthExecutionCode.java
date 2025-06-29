package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExecutionCode implements ExecutionCode{

    USER_LOGIN_SUCCESS(1000, "User login successful", HttpStatus.OK),
    USER_LOGOUT_SUCCESS(1002, "User logout successful", HttpStatus.OK),
    USER_REGISTRATION_SUCCESS(1003, "User registration successful", HttpStatus.CREATED),
    INVALID_CREDENTIALS(1104, "Invalid credentials provided", HttpStatus.UNAUTHORIZED),
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
