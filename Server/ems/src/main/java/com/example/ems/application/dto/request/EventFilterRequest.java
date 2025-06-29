package com.example.ems.application.dto.request;

import com.example.ems.infrastructure.constant.enums.EventVisibility;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.UUID;

public record EventFilterRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant endDate,

        String location,

        EventVisibility visibility,

        UUID hostId
) {}
