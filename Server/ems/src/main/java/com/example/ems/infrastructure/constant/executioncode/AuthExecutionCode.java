package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExecutionCode implements ExecutionCode{

    USER_LOGIN_SUCCESS(1000, "User login successful", HttpStatus.OK),
    INVALID_CREDENTIALS(1001, "Invalid credentials provided", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
