package com.example.ems.application.service;

import com.example.ems.infrastructure.constant.enums.AttendenceStatus;

import java.util.UUID;

public interface AttendanceService {
    AttendenceStatus checkEventStatus(UUID eventId, UUID userId);
}
