package com.example.ems.infrastructure.exceptions;

import com.example.ems.infrastructure.constant.executioncode.ExecutionCode;
import lombok.Getter;

@Getter
public class AttendanceException extends RuntimeException {
    private final transient ExecutionCode executionCode;

    public AttendanceException(ExecutionCode executionCode) {
        super(executionCode.getMessage());
        this.executionCode = executionCode;
    }
}
