package com.example.ems.infrastructure.constant.executioncode;

import org.springframework.http.HttpStatus;

public interface ExecutionCode {
    int getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
