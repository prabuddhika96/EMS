package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.EventEntity;
import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.domain.model.Event;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.EventVisibility;
import com.example.ems.infrastructure.exceptions.EventException;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventRepositoryImplTest {

    @Mock
    private SpringDataEventRepository springDataEventRepository;

    @Mock
    private LoggingUtil logger;

    @InjectMocks
    private EventRepositoryImpl eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        CreateEventRequest request = new CreateEventRequest("Title", "Description", Instant.now(), Instant.now().plusSeconds(3600), "Location", EventVisibility.PUBLIC);
        CustomUserDetails user = mock(CustomUserDetails.class);
        UUID userId = UUID.randomUUID();
        when(user.getUserId()).thenReturn(userId);

        EventEntity saved = new EventEntity();
        saved.setId(UUID.randomUUID());
        when(springDataEventRepository.save(any(EventEntity.class))).thenReturn(saved);

        Event event = eventRepository.createEvent(request, user);
        assertThat(event).isNotNull();
    }

    @Test
    void testUpdateEvent_SuccessfulUpdate() {
        UUID eventId = UUID.randomUUID();
        UUID hostId = UUID.randomUUID();

        CreateEventRequest request = mock(CreateEventRequest.class);
        CustomUserDetails user = mock(CustomUserDetails.class);

        when(request.title()).thenReturn("Updated Title");
        when(request.description()).thenReturn("Updated Description");
        Instant now = Instant.now();
        when(request.startTime()).thenReturn(now);
        when(request.endTime()).thenReturn(now.plusSeconds(3600));
        when(request.location()).thenReturn("Updated Location");
        when(request.visibility()).thenReturn(EventVisibility.PUBLIC);

        EventEntity savedEntity = new EventEntity();
        savedEntity.setId(eventId);
        savedEntity.setTitle("Updated Title");
        savedEntity.setDescription("Updated Description");
        savedEntity.setStartTime(now);
        savedEntity.setEndTime(now.plusSeconds(3600));
        savedEntity.setLocation("Updated Location");
        savedEntity.setVisibility(EventVisibility.PUBLIC);
        savedEntity.setUpdatedAt(Instant.now());
        savedEntity.setUser(UserEntity.builder().id(hostId).build());

        when(springDataEventRepository.save(any(EventEntity.class))).thenReturn(savedEntity);

        Event result = eventRepository.updateEvent(eventId, request, user, hostId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals("Updated Title", result.getTitle());
    }


    @Test
    void testDeleteEvent_UnauthorizedUser() {
        UUID eventId = UUID.randomUUID();
        CustomUserDetails user = mock(CustomUserDetails.class);

        EventEntity event = new EventEntity();
        event.setUser(UserEntity.builder().id(UUID.randomUUID()).build());

        when(springDataEventRepository.findByIdAndIsDeletedFalse(eventId)).thenReturn(Optional.of(event));
        when(user.getUserId()).thenReturn(UUID.randomUUID());
        when(user.isAdmin()).thenReturn(false);

        assertThrows(EventException.class, () -> eventRepository.deleteEvent(eventId, user));
    }

//    @Test
//    void testFilterEvents() {
//        EventFilterRequest filter = new EventFilterRequest(null, null, "location", EventVisibility.PUBLIC, null);
//        Pageable pageable = PageRequest.of(0, 10);
//
//        EventEntity mockEntity = mock(EventEntity.class);
//        Page<EventEntity> page = new PageImpl<>(List.of(mockEntity));
//
//        when(springDataEventRepository.findAll((Example<EventEntity>) any(), eq(pageable))).thenReturn(page);
//
//        Page<Event> result = eventRepository.filterEvents(filter, pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//    }


    @Test
    void testFindUpcomingEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        EventEntity entity = new EventEntity();
        Page<EventEntity> page = new PageImpl<>(List.of(entity));

        when(springDataEventRepository.findByStartTimeAfterAndVisibilityAndIsDeletedFalse(any(), eq(EventVisibility.PUBLIC), eq(pageable)))
                .thenReturn(page);

        Page<Event> result = eventRepository.findUpcomingEvents(pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testFindEventsHostedByUser() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventEntity> page = new PageImpl<>(List.of(new EventEntity()));

        when(springDataEventRepository.findByUserIdAndIsDeletedFalse(userId, pageable)).thenReturn(page);

        Page<Event> result = eventRepository.findEventsHostedByUser(userId, pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testFindEventsAttendedByUser() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventEntity> page = new PageImpl<>(List.of(new EventEntity()));

        when(springDataEventRepository.findEventsUserIsAttending(userId, pageable)).thenReturn(page);

        Page<Event> result = eventRepository.findEventsAttendedByUser(userId, pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testGetEventById_Success() {
        UUID eventId = UUID.randomUUID();
        EventEntity entity = new EventEntity();
        when(springDataEventRepository.findByIdAndIsDeletedFalse(eventId)).thenReturn(Optional.of(entity));

        Event event = eventRepository.getEventById(eventId);
        assertThat(event).isNotNull();
    }

    @Test
    void testGetEventById_NotFound() {
        UUID eventId = UUID.randomUUID();
        when(springDataEventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventException.class, () -> eventRepository.getEventById(eventId));
    }

    @Test
    void testGetDistinctHosts() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        when(springDataEventRepository.findDistinctHostsByVisibility(EventVisibility.PUBLIC))
                .thenReturn(Optional.of(List.of(userEntity)));

        List<User> result = eventRepository.getDistinctHosts(EventVisibility.PUBLIC);
        assertThat(result).hasSize(1);
    }
}
