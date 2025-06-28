package com.example.ems.infrastructure.exceptions;

import com.example.ems.infrastructure.constant.executioncode.ExecutionCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private final transient ExecutionCode executionCode;

    public CommonException(ExecutionCode executionCode) {
        super(executionCode.getMessage());
        this.executionCode = executionCode;
    }
}
