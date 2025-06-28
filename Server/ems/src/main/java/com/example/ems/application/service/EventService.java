package com.example.ems.application.service;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser);

    Event updateEvent(UUID eventId, @Valid CreateEventRequest updateRequest, CustomUserDetails currentUser);

    void deleteEvent(UUID eventId, CustomUserDetails currentUser);

    Page<Event> filterEvents(EventFilterRequest filterRequest, Pageable pageable);

    Page<Event> getUpcomingEvents(Pageable pageable);

    Page<Event> getEventsHostedByUser(UUID userId, Pageable pageable);

    Page<Event> getEventsAttendedByUser(UUID userId, Pageable pageable);


}
