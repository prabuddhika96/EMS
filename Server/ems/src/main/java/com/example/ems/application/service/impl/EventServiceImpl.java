package com.example.ems.application.service.impl;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.application.repository.EventRepository;
import com.example.ems.application.service.EventService;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LoggingUtil logger;

    @Override
    public Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser) {
        logger.info("Creating event with request: " + createEventRequest);
        return eventRepository.createEvent(createEventRequest, currentUser);
    }

    @Override
    public Event updateEvent(UUID eventId, CreateEventRequest updateRequest, CustomUserDetails currentUser) {
        logger.info("Updating event with ID: " + eventId);
        return eventRepository.updateEvent(eventId, updateRequest, currentUser);
    }

    @Override
    public void deleteEvent(UUID eventId, CustomUserDetails currentUser) {
        logger.info("Deleting event with ID: " + eventId);
        eventRepository.deleteEvent(eventId, currentUser);
    }

    @Override
    public Page<Event> filterEvents(EventFilterRequest filterRequest, Pageable pageable) {
        return eventRepository.filterEvents(filterRequest, pageable);
    }

    @Override
    public Page<Event> getUpcomingEvents(Pageable pageable) {
        logger.info("Fetching upcoming events...");
        return eventRepository.findUpcomingEvents(pageable);
    }

    @Override
    public List<Event> getEventsHostedByUser(UUID userId) {
        return eventRepository.findEventsHostedByUser(userId);
    }

    @Override
    public List<Event> getEventsAttendedByUser(UUID userId) {
        return eventRepository.findEventsAttendedByUser(userId);
    }

}
