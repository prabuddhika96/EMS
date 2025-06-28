package com.example.ems.infrastructure.mapper;

import com.example.ems.adapter.outbound.postgres.entity.AttendanceEntity;
import com.example.ems.domain.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {
    public static Attendance toDomain(AttendanceEntity entity) {
        if (entity == null) return null;

        Attendance attendance = new Attendance();
        attendance.setId(entity.getId());
        attendance.setStatus(entity.getStatus());
        attendance.setRespondedAt(entity.getRespondedAt());

        if (entity.getUser() != null) {
            attendance.setUserId(entity.getUser().getId());
        }

        if (entity.getEvent() != null) {
            attendance.setEventId(entity.getEvent().getId());
        }

        return attendance;
    }

    public static AttendanceEntity toEntity(Attendance domain) {
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(domain.getId());
        entity.setStatus(domain.getStatus());
        entity.setRespondedAt(domain.getRespondedAt());

        // Set event and user externally where full objects are available
        return entity;
    }
}