package com.example.ems.domain.model;

import com.example.ems.infrastructure.constant.enums.EventVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private UUID id;
    private String title;
    private String description;
    private UUID hostId;
    private Instant startTime;
    private Instant endTime;
    private String location;
    private EventVisibility visibility;
    private Instant createdAt;
    private Instant updatedAt;
}
