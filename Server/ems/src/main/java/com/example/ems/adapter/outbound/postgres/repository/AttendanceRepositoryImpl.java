package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.AttendanceEntity;
import com.example.ems.adapter.outbound.postgres.entity.EventEntity;
import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.repository.AttendanceRepository;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.exceptions.AttendanceException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

interface SpringDataAttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {
    Optional<AttendanceEntity> findByEventIdAndUserId(UUID eventId, UUID userId);
    AttendanceEntity findByUserIdAndEventId(UUID userId, UUID eventId);
}

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final SpringDataAttendanceRepository springDataAttendanceRepository;
    private final LoggingUtil logger;
    private final EntityManager entityManager;

    @Override
    public AttendenceStatus checkEventStatus(UUID eventId, UUID userId) {
        try {
            logger.info(String.format("Checking attendance status for eventId: %s and userId: %s", eventId, userId));
            return springDataAttendanceRepository
                    .findByEventIdAndUserId(eventId, userId)
                    .map(AttendanceEntity::getStatus)
                    .orElseThrow(() -> new AttendanceException(AttendenceExecutionCode.ATTENDENCE_NOT_FOUND));
        } catch (AttendanceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while attending event: " + e.getMessage());
            throw new AttendanceException(AttendenceExecutionCode.ATTENDENCE_CREATION_FAILED);
        }
    }

    @Override
    public void attendEvent(UUID eventId, UUID userId, AttendenceStatus status){
        try {
            EventEntity event = entityManager.getReference(EventEntity.class, eventId);
            UserEntity user = entityManager.getReference(UserEntity.class, userId);

            AttendanceEntity existing = springDataAttendanceRepository.findByUserIdAndEventId(userId, eventId);
            if (existing != null) {
                throw new AttendanceException(AttendenceExecutionCode.USER_ALREADY_ATTENDING_EVENT);
            }

            AttendanceEntity attendance = AttendanceEntity.builder()
                    .event(event)
                    .user(user)
                    .status(status)
                    .respondedAt(Instant.now())
                    .build();

            springDataAttendanceRepository.save(attendance);
        } catch (AttendanceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error while attending event: " + e.getMessage());
            throw new AttendanceException(AttendenceExecutionCode.ATTENDENCE_CREATION_FAILED);
        }
    }

}
