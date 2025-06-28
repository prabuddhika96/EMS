package com.example.ems.application.service.impl;

import com.example.ems.application.repository.UserRepository;
import com.example.ems.application.service.UserService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserById(UUID id) {
        return userRepository.getUserById(id);
    }

}
