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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @PutMapping("/update/{eventId}")
    public ResponseEntity<ApiCommonResponse<Event>> updateEvent(
            @PathVariable UUID eventId,
            @RequestBody @Valid CreateEventRequest updateRequest,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        logger.info("Update request for eventId: " + eventId + ", by userId: " + currentUser.getUserId());

        return ApiCommonResponse.create(EventExecutionCode.EVENT_UPDATED_SUCCESS,
                eventService.updateEvent(eventId, updateRequest, currentUser));
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<ApiCommonResponse<Void>> deleteEvent(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        logger.info("Delete request for eventId: " + eventId + ", by userId: " + currentUser.getUserId());

        eventService.deleteEvent(eventId, currentUser);

        return ApiCommonResponse.create(EventExecutionCode.EVENT_DELETED_SUCCESS, null);
    }


}
