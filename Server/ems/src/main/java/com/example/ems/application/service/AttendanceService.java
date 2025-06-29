package com.example.ems.application.service;

import com.example.ems.application.dto.response.AttendingUserResponse;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {
    AttendenceStatus checkEventStatus(UUID eventId, UUID userId);

    void attendEvent(UUID eventId, UUID userId, AttendenceStatus status);

    Page<AttendingUserResponse> getAttendingUsersByEventId(UUID eventId, Pageable pageable);

}
