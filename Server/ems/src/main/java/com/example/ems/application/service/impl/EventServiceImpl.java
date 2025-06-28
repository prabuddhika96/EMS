package com.example.ems.application.service.impl;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.repository.EventRepository;
import com.example.ems.application.service.EventService;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.mapper.EventMapper;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
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


}
