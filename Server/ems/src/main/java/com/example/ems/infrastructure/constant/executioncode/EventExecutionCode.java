package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventExecutionCode implements ExecutionCode{
    EVENT_CREATED_SUCCESS(3000, "Event created successfully", HttpStatus.CREATED),
    EVENT_UPDATED_SUCCESS(3001, "Event updated successfully", HttpStatus.OK),
    EVENT_DELETED_SUCCESS(3002, "Event deleted successfully", HttpStatus.OK),
    EVENT_FETCHED_SUCCESS(3003, "Event fetched successfully", HttpStatus.OK),
    EVENT_LIST_FETCHED_SUCCESS(3004, "Event list fetched successfully", HttpStatus.OK),

    EVENT_NOT_FOUND(3005, "Event not found", HttpStatus.NOT_FOUND),
    EVENT_CREATION_FAILED(3006, "Event creation failed", HttpStatus.BAD_REQUEST),
    EVENT_UPDATE_FAILED(3007, "Event update failed", HttpStatus.BAD_REQUEST),
    EVENT_DELETION_FAILED(3008, "Event deletion failed", HttpStatus.BAD_REQUEST),
    EVENT_FETCH_FAILED(3009, "Event fetch failed", HttpStatus.BAD_REQUEST),
    EVENT_UPDATE_FORBIDDEN(3010, "Event update forbidden", HttpStatus.FORBIDDEN),
    EVENT_FILTER_FAILED(3011, "Event filter failed", HttpStatus.BAD_REQUEST),
    EVENTS_FETCH_FAILED(3012, "Events fetch failed", HttpStatus.BAD_REQUEST),
    EVENT_LIST_EMPTY(3013, "Event list is empty", HttpStatus.OK),
    NO_EVENTS_FOUND(3014, "No events found", HttpStatus.NOT_FOUND),
    EVENT_ACTION_NOT_ALLOWED(3015, "Event action not allowed", HttpStatus.METHOD_NOT_ALLOWED);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
