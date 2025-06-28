package com.example.ems.adapter.outbound.postgres.entity;

import com.example.ems.infrastructure.constant.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Instant createdAt;

    private Instant updatedAt;
}
