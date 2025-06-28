package com.example.ems.application.dto;

import com.example.ems.infrastructure.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;
}
