package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.EventEntity;
import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.CreateEventRequest;
import com.example.ems.application.repository.EventRepository;
import com.example.ems.domain.model.Event;
import com.example.ems.infrastructure.constant.executioncode.EventExecutionCode;
import com.example.ems.infrastructure.exceptions.EventException;
import com.example.ems.infrastructure.security.userdetails.CustomUserDetails;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

import static com.example.ems.infrastructure.mapper.EventMapper.createEventRequestToEntity;
import static com.example.ems.infrastructure.mapper.EventMapper.toDomain;

interface SpringDataEventRepository extends JpaRepository<EventEntity, UUID> {
}

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    private final SpringDataEventRepository springDataEventRepository;
    private final LoggingUtil logger;

    @Override
    public Event createEvent(CreateEventRequest createEventRequest, CustomUserDetails currentUser) {
        try {
            logger.info("Saving event: " + createEventRequest);

            EventEntity eventEntity = createEventRequestToEntity(createEventRequest);
            eventEntity.setUser(UserEntity.builder()
                    .id(currentUser.getUserId())
                    .build());
            EventEntity savedEventEntity = springDataEventRepository.save(eventEntity);
            return toDomain(savedEventEntity);
        } catch (EventException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving event: " + e.getMessage());
            throw new EventException(EventExecutionCode.EVENT_CREATION_FAILED);
        }
    }

    @Override
    public Event updateEvent(UUID eventId, CreateEventRequest updateRequest, CustomUserDetails currentUser) {
        try {
            logger.info("Updating event with ID: " + eventId + " by user: " + currentUser.getUsername());

            EventEntity existing = getEventEntity(eventId, currentUser, "update");

            existing.setTitle(updateRequest.title());
            existing.setDescription(updateRequest.description());
            existing.setStartTime(updateRequest.startTime());
            existing.setEndTime(updateRequest.endTime());
            existing.setLocation(updateRequest.location());
            existing.setVisibility(updateRequest.visibility());
            existing.setUpdatedAt(Instant.now());

            EventEntity saved = springDataEventRepository.save(existing);
            return toDomain(saved);

        } catch (EventException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating event: " + e.getMessage());
            throw new EventException(EventExecutionCode.EVENT_UPDATE_FAILED);
        }
    }



    @Override
    public void deleteEvent(UUID eventId, CustomUserDetails currentUser) {
        try {
            logger.info("Deleting event with ID: " + eventId + " by user: " + currentUser.getUsername());

            EventEntity eventEntity = getEventEntity(eventId, currentUser, "delete");

            springDataEventRepository.delete(eventEntity);
            logger.info("Event deleted successfully: " + eventId);
        } catch (EventException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting event: " + e.getMessage());
            throw new EventException(EventExecutionCode.EVENT_DELETION_FAILED);
        }
    }


    private EventEntity getEventEntity(UUID eventId, CustomUserDetails currentUser, String action) {
        EventEntity existing = springDataEventRepository.findById(eventId)
                .orElseThrow(() -> new EventException(EventExecutionCode.EVENT_NOT_FOUND));

        boolean isHost = existing.getUser().getId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isHost && !isAdmin) {
            logger.error("User: " + currentUser.getUsername() + " is not authorized to " + action + " event with ID: " + eventId);
            throw new EventException(EventExecutionCode.EVENT_UPDATE_FORBIDDEN);
        }
        return existing;
    }

}
