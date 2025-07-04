package com.example.ems.application.repository;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.EventVisibility;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventRepository {
    Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser);

    Event updateEvent(UUID eventId, CreateEventRequest updateRequest, CustomUserDetails currentUser, UUID hostId);

    void deleteEvent(UUID eventId, CustomUserDetails currentUser);

    Page<Event> filterEvents(EventFilterRequest filterRequest, Pageable pageable);

    Page<Event> findUpcomingEvents(Pageable pageable);

    Page<Event> findEventsHostedByUser(UUID userId, Pageable pageable);

    Page<Event> findEventsAttendedByUser(UUID userId, Pageable pageable);

    Event getEventById(UUID eventId);

    List<User> getDistinctHosts(EventVisibility visibility);
}
