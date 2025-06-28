package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.service.EventService;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.constant.executioncode.EventExecutionCode;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final LoggingUtil logger;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('EVENT_CREATE')")
    public ResponseEntity<ApiCommonResponse<Event>> createEvent(@RequestBody @Valid CreateEventRequest createEventRequest, @AuthenticationPrincipal CustomUserDetails currentUser) {
        logger.info("Creating event with request: " + createEventRequest.toString());

        return ApiCommonResponse.create(EventExecutionCode.EVENT_CREATED_SUCCESS,
                eventService.createEvent(createEventRequest, currentUser));
    }
}
