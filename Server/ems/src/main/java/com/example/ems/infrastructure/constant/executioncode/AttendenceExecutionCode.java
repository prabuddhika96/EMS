package com.example.ems.infrastructure.constant.executioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendenceExecutionCode implements ExecutionCode{
    ATTENDENCE_CREATED(4000, "Attendence created successfully", HttpStatus.CREATED),
    ATTENDENCE_NOT_FOUND(4001, "Attendence not found", HttpStatus.NOT_FOUND),
    ATTENDENCE_UPDATED(4002, "Attendence updated successfully", HttpStatus.OK),
    ATTENDENCE_DELETED(4003, "Attendence deleted successfully", HttpStatus.OK),
    ATTENDENCE_FOUND(4004, "Attendence found", HttpStatus.OK),;


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
