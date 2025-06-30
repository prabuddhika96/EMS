package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.AttendanceEntity;
import com.example.ems.application.dto.response.AttendingUserResponse;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.exceptions.AttendanceException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AttendanceRepositoryImplTest {

    @Mock
    private SpringDataAttendanceRepository springDataAttendanceRepository;

    @Mock
    private LoggingUtil logger;

    @InjectMocks
    private AttendanceRepositoryImpl attendanceRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckEventStatus_Success() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setStatus(AttendenceStatus.GOING);

        when(springDataAttendanceRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.of(attendance));

        AttendenceStatus status = attendanceRepository.checkEventStatus(eventId, userId);
        assertThat(status).isEqualTo(AttendenceStatus.GOING);
    }

    @Test
    void testCheckEventStatus_NotFound() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(springDataAttendanceRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.empty());

        AttendanceException exception = assertThrows(AttendanceException.class,
                () -> attendanceRepository.checkEventStatus(eventId, userId));

        assertThat(exception.getExecutionCode()).isEqualTo(AttendenceExecutionCode.ATTENDENCE_NOT_FOUND);
    }

    @Test
    void testAttendEvent_NewRecord() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AttendenceStatus status = AttendenceStatus.GOING;

        when(springDataAttendanceRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.empty());

        attendanceRepository.attendEvent(eventId, userId, status);

        verify(springDataAttendanceRepository).save(any(AttendanceEntity.class));
    }

    @Test
    void testAttendEvent_UpdateExisting() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AttendenceStatus newStatus = AttendenceStatus.GOING;

        AttendanceEntity existing = new AttendanceEntity();
        existing.setStatus(AttendenceStatus.GOING);

        when(springDataAttendanceRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.of(existing));

        attendanceRepository.attendEvent(eventId, userId, newStatus);

        assertThat(existing.getStatus()).isEqualTo(newStatus);
        verify(springDataAttendanceRepository).save(existing);
    }

    @Test
    void testGetAttendingUsersByEventId_Success() {
        UUID eventId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        AttendingUserResponse user = AttendingUserResponse.builder()
                .userId(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .status(AttendenceStatus.GOING)
                .respondedAt(Instant.now())
                .build();

        Page<AttendingUserResponse> mockPage = new PageImpl<>(List.of(user));

        when(springDataAttendanceRepository.findAttendingUsersByEventId(eventId, pageable))
                .thenReturn(mockPage);

        Page<AttendingUserResponse> result = attendanceRepository.getAttendingUsersByEventId(eventId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test User");
    }
}
