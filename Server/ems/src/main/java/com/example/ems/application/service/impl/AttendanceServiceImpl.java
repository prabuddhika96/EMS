package com.example.ems.application.service.impl;

import com.example.ems.application.repository.AttendanceRepository;
import com.example.ems.application.service.AttendanceService;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final LoggingUtil logger;


    @Override
    public AttendenceStatus checkEventStatus(UUID eventId, UUID userId) {
        logger.info("Checking attendance status for eventId: " + eventId + " and userId: " + userId);

        return attendanceRepository.checkEventStatus(eventId, userId);
    }
}
