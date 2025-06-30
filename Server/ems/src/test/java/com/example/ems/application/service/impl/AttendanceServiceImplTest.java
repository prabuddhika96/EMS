package com.example.ems.application.service.impl;

import com.example.ems.application.dto.response.AttendingUserResponse;
import com.example.ems.application.repository.AttendanceRepository;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

class AttendanceServiceImplTest {
    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private LoggingUtil logger;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkEventStatus_ShouldReturnStatus() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(attendanceRepository.checkEventStatus(eventId, userId))
                .thenReturn(AttendenceStatus.GOING);

        AttendenceStatus status = attendanceService.checkEventStatus(eventId, userId);

        assertThat(status).isEqualTo(AttendenceStatus.GOING);
        verify(logger).info("Checking attendance status for eventId: " + eventId + " and userId: " + userId);
    }

    @Test
    void attendEvent_ShouldCallRepositoryWithCorrectArguments() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AttendenceStatus status = AttendenceStatus.MAYBE;

        attendanceService.attendEvent(eventId, userId, status);

        verify(attendanceRepository).attendEvent(eventId, userId, status);
    }

    @Test
    void getAttendingUsersByEventId_ShouldReturnPage() {
        UUID eventId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);
        AttendingUserResponse response = new AttendingUserResponse();
        Page<AttendingUserResponse> expectedPage = new PageImpl<>(List.of(response));

        when(attendanceRepository.getAttendingUsersByEventId(eventId, pageable))
                .thenReturn(expectedPage);

        Page<AttendingUserResponse> result = attendanceService.getAttendingUsersByEventId(eventId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(response);
    }
}
