package com.example.ems.application.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User getUserById (UUID id);
    Optional<UserEntity> getUserByEmail (String email);
    User save(UserRegistrationRequest userRegistrationRequest);

    Page<User> getUserList(Pageable pageable);

    String changeUserRole(UUID userId, String newRole);
}
