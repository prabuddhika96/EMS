package com.example.ems.application.dto.response;

import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendingUserResponse{
    private UUID userId;
    private String name;
    private String email;
    private AttendenceStatus status;
    private Instant respondedAt;
}