package com.example.ems.application.dto.request;

import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendEventRequest(
        @NotNull(message = "Attendance status is required")
        AttendenceStatus status
) {}