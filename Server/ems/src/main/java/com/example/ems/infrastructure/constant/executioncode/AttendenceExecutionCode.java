package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendenceExecutionCode implements ExecutionCode{
    EVENT_ATTENDED_SUCCESS(4000, "Event attended successfully", HttpStatus.OK),
    ATTENDENCE_NOT_FOUND(4001, "Attendence not found", HttpStatus.NOT_FOUND),
    ATTENDENCE_UPDATED(4002, "Attendence updated successfully", HttpStatus.OK),
    ATTENDENCE_DELETED(4003, "Attendence deleted successfully", HttpStatus.OK),
    ATTENDENCE_FOUND(4004, "Attendence found", HttpStatus.OK),
    USER_ALREADY_ATTENDING_EVENT(4005, "User is already attending this event", HttpStatus.BAD_REQUEST),
    ATTENDENCE_CREATION_FAILED(4006, "Failed to create attendence", HttpStatus.INTERNAL_SERVER_ERROR),;


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
