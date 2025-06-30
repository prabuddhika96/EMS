package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.AttendanceEntity;
import com.example.ems.adapter.outbound.postgres.entity.EventEntity;
import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.response.AttendingUserResponse;
import com.example.ems.application.repository.AttendanceRepository;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.exceptions.AttendanceException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataAttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {
    Optional<AttendanceEntity> findByEventIdAndUserId(UUID eventId, UUID userId);
    Optional<AttendanceEntity> findByUserIdAndEventId(UUID userId, UUID eventId);

    @Query("SELECT a.user FROM AttendanceEntity a WHERE a.event.id = :eventId")
    List<UserEntity> findUsersByEventId(UUID eventId);

    @Query("""
    SELECT new com.example.ems.application.dto.response.AttendingUserResponse(
        u.id, u.name, u.email, a.status, a.respondedAt
    )
    FROM AttendanceEntity a
    JOIN a.user u
    WHERE a.event.id = :eventId
""")
    Page<AttendingUserResponse> findAttendingUsersByEventId(UUID eventId, Pageable pageable);
}

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final SpringDataAttendanceRepository springDataAttendanceRepository;
    private final LoggingUtil logger;
    private final static String ERROR_TEXT = "Error while attending event: ";

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
            logger.error(ERROR_TEXT + e.getMessage());
            throw new AttendanceException(AttendenceExecutionCode.ATTENDENCE_CREATION_FAILED);
        }
    }

    @Override
    public void attendEvent(UUID eventId, UUID userId, AttendenceStatus status){
        try {
            Optional<AttendanceEntity> optionalAttendence = springDataAttendanceRepository.findByUserIdAndEventId(userId, eventId);
            if (optionalAttendence.isPresent()) {
                AttendanceEntity existing = optionalAttendence.get();
                logger.info(String.format("Updating attendance status for userId: %s and eventId: %s", userId, eventId));

                existing.setStatus(status);
                existing.setRespondedAt(Instant.now());
                springDataAttendanceRepository.save(existing);
                return;
            }

            AttendanceEntity attendance = AttendanceEntity.builder()
                    .event(EventEntity.builder().id(eventId).build())
                    .user(UserEntity.builder().id(userId).build())
                    .status(status)
                    .respondedAt(Instant.now())
                    .build();

            springDataAttendanceRepository.save(attendance);
        } catch (AttendanceException e) {
            throw e;
        } catch (Exception e) {
            logger.error(ERROR_TEXT + e.getMessage());
            throw new AttendanceException(AttendenceExecutionCode.ATTENDENCE_CREATION_FAILED);
        }
    }

    @Override
    public Page<AttendingUserResponse> getAttendingUsersByEventId(UUID eventId, Pageable pageable) {
        try {
            logger.info("Fetching attending users for eventId: " + eventId);
            return springDataAttendanceRepository.findAttendingUsersByEventId(eventId, pageable);
        } catch (AttendanceException e) {
            throw e;
        } catch (Exception e) {
            logger.error(ERROR_TEXT + e.getMessage());
            throw new AttendanceException(AttendenceExecutionCode.ATTENDENCE_FETCH_FAILED);
        }
    }

}
