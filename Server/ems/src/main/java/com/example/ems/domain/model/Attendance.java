package com.example.ems.domain.model;

import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    private Integer id;
    private UUID userId;
    private UUID eventId;
    private AttendenceStatus status;
    private Instant respondedAt;
}
