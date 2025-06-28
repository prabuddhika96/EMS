package com.example.ems.application.service;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import jakarta.validation.Valid;

import java.util.UUID;

public interface EventService {
    Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser);

    Event updateEvent(UUID eventId, @Valid CreateEventRequest updateRequest, CustomUserDetails currentUser);

    void deleteEvent(UUID eventId, CustomUserDetails currentUser);

}
