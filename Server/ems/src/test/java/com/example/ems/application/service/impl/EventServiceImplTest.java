package com.example.ems.application.service.impl;

import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.application.repository.EventRepository;
import com.example.ems.domain.model.Event;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.EventVisibility;
import com.example.ems.infrastructure.constant.executioncode.EventExecutionCode;
import com.example.ems.infrastructure.exceptions.EventException;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LoggingUtil logger;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Event mockEvent(UUID eventId, UUID hostId) {
        return Event.builder()
                .id(eventId)
                .hostId(hostId)
                .title("Test Event")
                .description("Test Description")
                .startTime(Instant.now())
                .endTime(Instant.now().plusSeconds(3600))
                .location("Location")
                .visibility(EventVisibility.PUBLIC)
                .build();
    }

    private CustomUserDetails mockUser(UUID userId, boolean isAdmin) {
        CustomUserDetails user = mock(CustomUserDetails.class);
        when(user.getUserId()).thenReturn(userId);
        when(user.isAdmin()).thenReturn(isAdmin);
        return user;
    }

    @Test
    void testCreateEvent_Success() {
        CreateEventRequest request = mock(CreateEventRequest.class);
        CustomUserDetails user = mockUser(UUID.randomUUID(), false);
        Event expected = mockEvent(UUID.randomUUID(), user.getUserId());

        when(eventRepository.createEvent(request, user)).thenReturn(expected);

        Event result = eventService.createEvent(request, user);
        assertEquals(expected, result);
    }

    @Test
    void testUpdateEvent_AuthorizedUser_Success() {
        UUID eventId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        CustomUserDetails user = mockUser(hostId, false);
        CreateEventRequest updateRequest = mock(CreateEventRequest.class);
        Event mockEvent = mockEvent(eventId, hostId);
        Event updatedEvent = mockEvent(eventId, hostId);

        when(eventRepository.getEventById(eventId)).thenReturn(mockEvent);
        when(eventRepository.updateEvent(eventId, updateRequest, user, hostId)).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(eventId, updateRequest, user);
        assertNotNull(result);
        assertEquals(eventId, result.getId());
    }

    @Test
    void testUpdateEvent_UnauthorizedUser_ThrowsException() {
        UUID eventId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        CustomUserDetails user = mockUser(anotherUserId, false);
        CreateEventRequest updateRequest = mock(CreateEventRequest.class);
        Event mockEvent = mockEvent(eventId, hostId);

        when(eventRepository.getEventById(eventId)).thenReturn(mockEvent);

        EventException exception = assertThrows(EventException.class, () ->
                eventService.updateEvent(eventId, updateRequest, user)
        );

        assertEquals(EventExecutionCode.EVENT_ACTION_NOT_ALLOWED, exception.getExecutionCode());
    }

    @Test
    void testDeleteEvent_AuthorizedUser_Success() {
        UUID eventId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        CustomUserDetails user = mockUser(hostId, false);
        Event mockEvent = mockEvent(eventId, hostId);

        when(eventRepository.getEventById(eventId)).thenReturn(mockEvent);
        doNothing().when(eventRepository).deleteEvent(eventId, user);

        assertDoesNotThrow(() -> eventService.deleteEvent(eventId, user));
    }

    @Test
    void testDeleteEvent_UnauthorizedUser_ThrowsException() {
        UUID eventId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CustomUserDetails user = mockUser(userId, false);
        Event mockEvent = mockEvent(eventId, hostId);

        when(eventRepository.getEventById(eventId)).thenReturn(mockEvent);

        EventException exception = assertThrows(EventException.class, () ->
                eventService.deleteEvent(eventId, user)
        );

        assertEquals(EventExecutionCode.EVENT_ACTION_NOT_ALLOWED, exception.getExecutionCode());
    }

    @Test
    void testFilterEvents_Success() {
        EventFilterRequest request = mock(EventFilterRequest.class);
        Pageable pageable = mock(Pageable.class);
        Page<Event> page = new PageImpl<>(Collections.singletonList(mockEvent(UUID.randomUUID(), UUID.randomUUID())));

        when(eventRepository.filterEvents(request, pageable)).thenReturn(page);

        Page<Event> result = eventService.filterEvents(request, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetUpcomingEvents_Success() {
        Pageable pageable = mock(Pageable.class);
        Page<Event> page = new PageImpl<>(Collections.singletonList(mockEvent(UUID.randomUUID(), UUID.randomUUID())));

        when(eventRepository.findUpcomingEvents(pageable)).thenReturn(page);

        Page<Event> result = eventService.getUpcomingEvents(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEventsHostedByUser_Success() {
        Pageable pageable = mock(Pageable.class);
        UUID userId = UUID.randomUUID();
        Page<Event> page = new PageImpl<>(Collections.singletonList(mockEvent(UUID.randomUUID(), userId)));

        when(eventRepository.findEventsHostedByUser(userId, pageable)).thenReturn(page);

        Page<Event> result = eventService.getEventsHostedByUser(userId, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEventsAttendedByUser_Success() {
        Pageable pageable = mock(Pageable.class);
        UUID userId = UUID.randomUUID();
        Page<Event> page = new PageImpl<>(Collections.singletonList(mockEvent(UUID.randomUUID(), UUID.randomUUID())));

        when(eventRepository.findEventsAttendedByUser(userId, pageable)).thenReturn(page);

        Page<Event> result = eventService.getEventsAttendedByUser(userId, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEventById_Success() {
        UUID eventId = UUID.randomUUID();
        Event event = mockEvent(eventId, UUID.randomUUID());

        when(eventRepository.getEventById(eventId)).thenReturn(event);

        Event result = eventService.getEventById(eventId);
        assertEquals(eventId, result.getId());
    }

    @Test
    void testGetDistinctHosts_Success() {
        List<User> users = List.of(User.builder().id(UUID.randomUUID()).name("Host").build());
        when(eventRepository.getDistinctHosts(EventVisibility.PUBLIC)).thenReturn(users);

        List<User> result = eventService.getDistinctHosts(EventVisibility.PUBLIC);
        assertEquals(1, result.size());
    }
}
