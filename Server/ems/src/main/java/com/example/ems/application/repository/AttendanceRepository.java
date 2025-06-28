package com.example.ems.application.repository;

import com.example.ems.infrastructure.constant.enums.AttendenceStatus;

import java.util.UUID;

public interface AttendanceRepository {
    AttendenceStatus checkEventStatus(UUID eventId, UUID userId);

    void attendEvent(UUID eventId, UUID userId, AttendenceStatus status);
}
