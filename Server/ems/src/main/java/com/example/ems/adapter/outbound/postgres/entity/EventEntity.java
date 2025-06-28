package com.example.ems.adapter.outbound.postgres.entity;

import com.example.ems.infrastructure.constant.enums.EventVisibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private String location;

    @Enumerated(EnumType.STRING)
    private EventVisibility visibility;

    private Instant createdAt;
    private Instant updatedAt;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "hostId", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<AttendanceEntity> attendances;
}
