package com.example.ems.application.repository;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;

import java.util.UUID;

public interface EventRepository {
    Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser);

    Event updateEvent(UUID eventId, CreateEventRequest updateRequest, CustomUserDetails currentUser);
}
