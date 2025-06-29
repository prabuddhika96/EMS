package com.example.ems.application.service.impl;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.application.repository.EventRepository;
import com.example.ems.application.service.EventService;
import com.example.ems.domain.model.Event;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.EventVisibility;
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
    public Page<Event> getEventsHostedByUser(UUID userId, Pageable pageable) {
        return eventRepository.findEventsHostedByUser(userId, pageable);
    }

    @Override
    public Page<Event> getEventsAttendedByUser(UUID userId, Pageable pageable) {
        return eventRepository.findEventsAttendedByUser(userId, pageable);
    }

    @Override
    public Event getEventById(UUID eventId) {
        return eventRepository.getEventById(eventId);
    }

    @Override
    public List<User> getDistinctHosts(EventVisibility visibility) {
        return eventRepository.getDistinctHosts(visibility);
    }

}
