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
}
