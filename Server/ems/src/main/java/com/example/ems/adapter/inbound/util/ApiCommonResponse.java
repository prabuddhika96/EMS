package com.example.ems.adapter.inbound.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public record ApiCommonResponse<T>(
        Integer code,
        String message,
        T data
) {
    public static <T> ResponseEntity<ApiCommonResponse<T>> create(HttpStatus status, int code, String msg, T data) {
        return ResponseEntity
                .status(status)
                .body(ApiCommonResponse.<T>builder()
                        .code(code)
                        .message(msg)
                        .data(data).build());
    }
}

