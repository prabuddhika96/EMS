package com.example.ems.infrastructure.mapper;

import com.example.ems.adapter.outbound.postgres.entity.EventEntity;
import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.domain.model.Event;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EventMapper {
    public static Event toDomain(EventEntity entity) {
        if (entity == null) return null;

        Event event = new Event();
        event.setId(entity.getId());
        event.setTitle(entity.getTitle());
        event.setDescription(entity.getDescription());
        event.setStartTime(entity.getStartTime());
        event.setEndTime(entity.getEndTime());
        event.setLocation(entity.getLocation());
        event.setVisibility(entity.getVisibility());
        event.setCreatedAt(entity.getCreatedAt());
        event.setUpdatedAt(entity.getUpdatedAt());
        event.setIsDeleted(entity.getIsDeleted());

        if (entity.getUser() != null) {
            event.setHostId(entity.getUser().getId());
        }

        return event;
    }

    public static EventEntity toEntity(Event domain) {
        EventEntity entity = new EventEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setLocation(domain.getLocation());
        entity.setVisibility(domain.getVisibility());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setIsDeleted(domain.getIsDeleted());
        return entity;
    }

    public static EventEntity createEventRequestToEntity(CreateEventRequest createEventRequest) {
        return EventEntity.builder()
                .title(createEventRequest.title())
                .description(createEventRequest.description())
                .startTime(createEventRequest.startTime())
                .endTime(createEventRequest.endTime())
                .location(createEventRequest.location())
                .visibility(createEventRequest.visibility())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .isDeleted(false)
                .build();
    }
}
