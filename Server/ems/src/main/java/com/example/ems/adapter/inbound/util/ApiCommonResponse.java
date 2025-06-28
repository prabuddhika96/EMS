package com.example.ems.adapter.inbound.util;

import com.example.ems.infrastructure.constant.executioncode.ExecutionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public record ApiCommonResponse<T>(
        Integer code,
        String message,
        T data
) {
    public static <T> ResponseEntity<ApiCommonResponse<T>> create(ExecutionCode executionCode, T data) {
        return ResponseEntity
                .status(executionCode.getHttpStatus())
                .body(ApiCommonResponse.<T>builder()
                        .code(executionCode.getCode())
                        .message(executionCode.getMessage())
                        .data(data).build());
    }
}

