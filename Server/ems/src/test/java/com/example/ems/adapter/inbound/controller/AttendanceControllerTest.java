package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.dto.request.AttendEventRequest;
import com.example.ems.application.dto.response.AttendingUserResponse;
import com.example.ems.application.service.AttendanceService;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AttendanceControllerTest {

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private LoggingUtil logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckEventStatus() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AttendenceStatus status = AttendenceStatus.GOING;

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(userId);
        when(userDetails.getUsername()).thenReturn("testuser@gmail.com");

        when(attendanceService.checkEventStatus(eventId, userId)).thenReturn(status);

        ResponseEntity<ApiCommonResponse<AttendenceStatus>> response =
                attendanceController.checkEventStatus(eventId, userDetails);

        assertNotNull(response);
        assertEquals(status, response.getBody().data());
        assertEquals(AttendenceExecutionCode.ATTENDENCE_FOUND.getCode(), response.getBody().code());

        verify(attendanceService).checkEventStatus(eventId, userId);
    }



    @Test
    void testAttendEvent() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        AttendEventRequest request = new AttendEventRequest(AttendenceStatus.GOING);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(userId);
        when(userDetails.getUsername()).thenReturn("testuser@gmail.com");

        ResponseEntity<ApiCommonResponse<String>> response =
                attendanceController.attendEvent(eventId, request, userDetails);

        assertNotNull(response);
        assertEquals( "User marked as GOING", response.getBody().data());
        assertEquals(AttendenceExecutionCode.EVENT_ATTENDED_SUCCESS.getCode(), response.getBody().code());

        verify(attendanceService).attendEvent(eventId, userId, request.status());
    }

    @Test
    void testGetAttendingUsers() {
        UUID eventId = UUID.randomUUID();
        int page = 0;
        int size = 2;

        AttendingUserResponse user1 = AttendingUserResponse.builder()
                .userId(UUID.randomUUID())
                .name("testuser1")
                .email("testuser1@gmail.com")
                .status(AttendenceStatus.GOING)
                .build();

        AttendingUserResponse user2 = AttendingUserResponse.builder()
                .userId(UUID.randomUUID())
                .name("testuser2")
                .email("testuser2@gmail.com")
                .status(AttendenceStatus.MAYBE)
                .build();

        Page<AttendingUserResponse> pageResult = new PageImpl<>(List.of(user1, user2));

        when(attendanceService.getAttendingUsersByEventId(eventId, PageRequest.of(page, size)))
                .thenReturn(pageResult);

        ResponseEntity<ApiCommonResponse<Page<AttendingUserResponse>>> response =
                attendanceController.getAttendingUsers(eventId, page, size);

        assertNotNull(response);
        assertThat(response.getBody()).isNotNull();
        assertEquals(2L, response.getBody().data().get().count());
        assertEquals(AttendenceExecutionCode.USERS_FETCHED_SUCCESS.getCode(), response.getBody().code());

        verify(attendanceService).getAttendingUsersByEventId(eventId, PageRequest.of(page, size));
    }
}
