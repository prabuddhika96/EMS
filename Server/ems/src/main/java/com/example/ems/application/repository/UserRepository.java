package com.example.ems.application.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.UserRegistrationRequest;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserEntity> getUserById (UUID id);
    Optional<UserEntity> getUserByEmail (String email);
    UserEntity save(UserRegistrationRequest userRegistrationRequest);
}
