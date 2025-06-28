package com.example.ems.application.service;

import com.example.ems.domain.model.User;

import java.util.UUID;

public interface UserService {
    User getUserById (UUID id);
}
