package com.example.ems.application.service;

import com.example.ems.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    User getUserById (UUID id);

    Page<User> getUserList(Pageable pageable);

    String changeUserRole(UUID userId, String newRole);
}
