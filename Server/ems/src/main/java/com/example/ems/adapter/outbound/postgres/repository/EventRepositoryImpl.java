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
        }catch (Exception e) {
            logger.error("Error saving event: " + e.getMessage());
            throw new EventException(EventExecutionCode.EVENT_CREATION_FAILED);
        }
    }
}
