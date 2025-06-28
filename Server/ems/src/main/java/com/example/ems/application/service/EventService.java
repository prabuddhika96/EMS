package com.example.ems.application.service;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;

public interface EventService {
    Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser);
}
