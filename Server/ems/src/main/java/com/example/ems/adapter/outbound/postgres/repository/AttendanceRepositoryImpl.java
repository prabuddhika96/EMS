package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.AttendanceEntity;
import com.example.ems.application.repository.AttendanceRepository;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.exceptions.AttendanceException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

interface SpringDataAttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {
    Optional<AttendanceEntity> findByEventIdAndUserId(UUID eventId, UUID userId);
}

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final SpringDataAttendanceRepository springDataAttendanceRepository;
    private final LoggingUtil logger;

    @Override
    public AttendenceStatus checkEventStatus(UUID eventId, UUID userId) {
        logger.info(String.format("Checking attendance status for eventId: %s and userId: %s", eventId, userId));
        return springDataAttendanceRepository
                .findByEventIdAndUserId(eventId, userId)
                .map(AttendanceEntity::getStatus)
                .orElseThrow(() -> new AttendanceException(AttendenceExecutionCode.ATTENDENCE_NOT_FOUND));
    }
}
