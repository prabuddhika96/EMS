package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.service.AttendanceService;
import com.example.ems.infrastructure.constant.enums.AttendenceStatus;
import com.example.ems.infrastructure.constant.executioncode.AttendenceExecutionCode;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendence")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final LoggingUtil logger;

    @GetMapping("/{eventId}/status")
    public ResponseEntity<ApiCommonResponse<AttendenceStatus>> checkEventStatus(@PathVariable UUID eventId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        logger.info(String.format("Checking attendance status for event: %s, user: %s", eventId, currentUser.getUsername()));

        return ApiCommonResponse.create(AttendenceExecutionCode.ATTENDENCE_FOUND, attendanceService.checkEventStatus(eventId, currentUser.getUserId()));
    }
}
