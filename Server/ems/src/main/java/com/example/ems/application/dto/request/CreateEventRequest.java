package com.example.ems.application.dto.request;

import com.example.ems.infrastructure.constant.enums.EventVisibility;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateEventRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotNull(message = "Start time is required")
        @FutureOrPresent(message = "Start time must be in the future")
        Instant startTime,

        @NotNull(message = "End time is required")
        @FutureOrPresent(message = "End time must be in the future")
        Instant endTime,

        @NotBlank(message = "Location is required")
        @Size(max = 255, message = "Location must be at most 255 characters")
        String location,

        @NotNull(message = "Visibility is required")
        EventVisibility visibility
) {}
