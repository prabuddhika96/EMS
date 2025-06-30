package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.dto.request.EventFilterRequest;
import com.example.ems.application.service.EventService;
import com.example.ems.domain.model.Event;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.EventVisibility;
import com.example.ems.infrastructure.constant.executioncode.EventExecutionCode;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @Mock
    private LoggingUtil logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        CreateEventRequest request = mock(CreateEventRequest.class);
        CustomUserDetails user = mock(CustomUserDetails.class);
        Event mockEvent = mock(Event.class);

        when(eventService.createEvent(request, user)).thenReturn(mockEvent);

        ResponseEntity<ApiCommonResponse<Event>> response = eventController.createEvent(request, user);

        assertThat(response.getBody().data()).isEqualTo(mockEvent);
        assertEquals(EventExecutionCode.EVENT_CREATED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testUpdateEvent() {
        UUID eventId = UUID.randomUUID();
        CreateEventRequest request = mock(CreateEventRequest.class);
        CustomUserDetails user = mock(CustomUserDetails.class);
        Event updatedEvent = mock(Event.class);

        when(eventService.updateEvent(eventId, request, user)).thenReturn(updatedEvent);

        var response = eventController.updateEvent(eventId, request, user);

        assertThat(response.getBody().data()).isEqualTo(updatedEvent);
        assertEquals(EventExecutionCode.EVENT_UPDATED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testDeleteEvent() {
        UUID eventId = UUID.randomUUID();
        CustomUserDetails user = mock(CustomUserDetails.class);

        var response = eventController.deleteEvent(eventId, user);

        assertThat(response.getBody().code()).isEqualTo(EventExecutionCode.EVENT_DELETED_SUCCESS.getCode());
        verify(eventService).deleteEvent(eventId, user);
    }

    @Test
    void testFilterEvents() {
        EventFilterRequest filter = new EventFilterRequest(
                null,
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(List.of(mock(Event.class)));

        when(eventService.filterEvents(filter, pageable)).thenReturn(page);

        var response = eventController.filterEvents(filter, pageable);

        assertThat(response.getBody().data().getContent()).hasSize(1);
        assertEquals(EventExecutionCode.EVENT_LIST_FETCHED_SUCCESS.getCode(), response.getBody().code());
    }


    @Test
    void testGetUpcomingEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(List.of(mock(Event.class)));

        when(eventService.getUpcomingEvents(pageable)).thenReturn(page);

        var response = eventController.getUpcomingEvents(0, 10);

        assertThat(response.getBody().data().getContent()).hasSize(1);
        assertEquals(EventExecutionCode.EVENT_LIST_FETCHED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testGetUserEventsByType_Hosting() {
        UUID userId = UUID.randomUUID();
        CustomUserDetails user = mock(CustomUserDetails.class);
        when(user.getUserId()).thenReturn(userId);

        Page<Event> page = new PageImpl<>(List.of(mock(Event.class)));
        when(eventService.getEventsHostedByUser(eq(userId), any())).thenReturn(page);

        var response = eventController.getUserEventsByType("hosting", 0, 10, user);

        assertEquals(EventExecutionCode.EVENT_LIST_FETCHED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testGetUserEventsByType_Invalid() {
        CustomUserDetails user = mock(CustomUserDetails.class);

        assertThatThrownBy(() -> eventController.getUserEventsByType("invalid", 0, 10, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid type");
    }

    @Test
    void testGetEventById() {
        UUID eventId = UUID.randomUUID();
        Event event = mock(Event.class);

        when(eventService.getEventById(eventId)).thenReturn(event);

        var response = eventController.getEventById(eventId);

        assertThat(response.getBody().data()).isEqualTo(event);
        assertEquals(EventExecutionCode.EVENT_FETCHED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testGetDistinctHosts_WithData() {
        List<User> hosts = List.of(mock(User.class));
        when(eventService.getDistinctHosts(EventVisibility.PUBLIC)).thenReturn(hosts);

        var response = eventController.getDistinctHosts(EventVisibility.PUBLIC);

        assertThat(response.getBody().data()).hasSize(1);
        assertEquals(EventExecutionCode.EVENT_LIST_FETCHED_SUCCESS.getCode(), response.getBody().code());
    }

    @Test
    void testGetDistinctHosts_Empty() {
        when(eventService.getDistinctHosts(EventVisibility.PUBLIC)).thenReturn(Collections.emptyList());

        ResponseEntity<ApiCommonResponse<List<User>>> distinctHosts = eventController.getDistinctHosts(EventVisibility.PUBLIC);

        assertThat(distinctHosts.getBody().data()).isEmpty();
        assertEquals(EventExecutionCode.NO_EVENTS_FOUND.getCode(), distinctHosts.getBody().code());
    }
}
