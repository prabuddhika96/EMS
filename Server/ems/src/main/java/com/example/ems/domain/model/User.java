package com.example.ems.domain.model;

import com.example.ems.infrastructure.constant.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String name;
    private String email;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;
}
