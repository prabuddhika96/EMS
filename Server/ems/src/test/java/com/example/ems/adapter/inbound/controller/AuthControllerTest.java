package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.dto.response.AuthResponse;
import com.example.ems.application.service.AuthService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.executioncode.AuthExecutionCode;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private LoggingUtil logger;

    private final User mockUser = User.builder()
            .id(UUID.randomUUID())
            .name("Test User")
            .email("test@example.com")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        AuthRequest request = new AuthRequest("testuser@gmail.com", "password123");
        AuthResponse mockResponse = AuthResponse.builder()
                .user(mockUser)
                .build();

        when(authService.authenticateLogin(request)).thenReturn(mockResponse);

        ResponseEntity<ApiCommonResponse<AuthResponse>> response = authController.login(request);

        assertThat(response.getBody().data()).isEqualTo(mockResponse);
        assertEquals(AuthExecutionCode.USER_LOGIN_SUCCESS.getCode(), response.getBody().code());
        verify(authService).authenticateLogin(request);
    }

    @Test
    void testRegister() {
        UserRegistrationRequest request = new UserRegistrationRequest("Test User", "testuser@gmail.com", "password123");
        AuthResponse mockResponse = AuthResponse.builder()
                .user(mockUser)
                .build();

        when(authService.registerUser(request)).thenReturn(mockResponse);

        ResponseEntity<ApiCommonResponse<AuthResponse>> response = authController.register(request);

        assertThat(response.getBody().data()).isEqualTo(mockResponse);
        assertEquals(AuthExecutionCode.USER_REGISTRATION_SUCCESS.getCode(), response.getBody().code());
        verify(authService).registerUser(request);
    }

    @Test
    void testLogout() {
        AuthResponse mockResponse = AuthResponse.builder()
                .user(null)
                .build();

        when(authService.logout()).thenReturn(mockResponse);

        ResponseEntity<ApiCommonResponse<AuthResponse>> response = authController.logout();

        assertThat(response.getBody().data()).isEqualTo(mockResponse);
        assertEquals(AuthExecutionCode.USER_LOGOUT_SUCCESS.getCode(), response.getBody().code());
        verify(authService).logout();
    }
}
