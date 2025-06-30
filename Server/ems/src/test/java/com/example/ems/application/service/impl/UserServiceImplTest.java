package com.example.ems.application.service.impl;

import com.example.ems.application.repository.UserRepository;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserList() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = new User(UUID.randomUUID(), "Test User", "testuser@gmail.com", UserRole.USER, null, null);
        Page<User> userPage = new PageImpl<>(List.of(user));

        when(userRepository.getUserList(pageable)).thenReturn(userPage);

        Page<User> result = userService.getUserList(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testChangeUserRole() {
        UUID userId = UUID.randomUUID();
        String role = "ADMIN";

        when(userRepository.changeUserRole(userId, role)).thenReturn("User role changed to ADMIN");

        String result = userService.changeUserRole(userId, role);
        assertEquals("User role changed to ADMIN", result);
    }

    @Test
    void testGetUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Test User", "testuser@gmail.com", UserRole.USER, null, null);

        when(userRepository.getUserById(userId)).thenReturn(user);

        User result = userService.getUserById(userId);
        assertNotNull(result);
        assertEquals("Test User", result.getName());
    }
}
