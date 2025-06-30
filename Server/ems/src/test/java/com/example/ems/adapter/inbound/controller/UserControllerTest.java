package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.service.UserService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.executioncode.UserExecutionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("testuser@gmail.com")
                .build();
    }

    @Test
    void testGetUserList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(mockUser));

        when(userService.getUserList(pageable)).thenReturn(userPage);

        ResponseEntity<ApiCommonResponse<Page<User>>> response = userController.getUserList(0, 10);

        assertThat(response.getBody().data().getContent()).hasSize(1);
        assertEquals(UserExecutionCode.USER_LIST_FOUND.getCode(), response.getBody().code());
        verify(userService).getUserList(pageable);
    }

    @Test
    void testChangeUserRole() {
        UUID userId = UUID.randomUUID();
        String newRole = "ADMIN";

        when(userService.changeUserRole(userId, newRole)).thenReturn("User role changed to ADMIN");

        ResponseEntity<ApiCommonResponse<String>> response = userController.changeUserRole(userId, newRole);

        assertEquals(UserExecutionCode.USER_ROLE_CHANGED_SUCCESS.getCode(), response.getBody().code());
        assertEquals("User role changed to ADMIN", response.getBody().data());
        verify(userService).changeUserRole(userId, newRole);
    }

    @Test
    void testGetUser() {
        when(userService.getUserById(any(UUID.class))).thenReturn(mockUser);

        User result = userController.getUser();

        assertThat(result).isEqualTo(mockUser);
        verify(userService).getUserById(any(UUID.class));
    }
}
