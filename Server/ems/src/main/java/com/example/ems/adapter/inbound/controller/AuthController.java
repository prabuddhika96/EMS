package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.dto.response.AuthResponse;
import com.example.ems.application.service.AuthService;
import com.example.ems.infrastructure.constant.executioncode.AuthExecutionCode;
import com.example.ems.infrastructure.utli.LoggingUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoggingUtil logger;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiCommonResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest authRequest) {
        logger.info("Login request received for username: " + authRequest.email());

        return ApiCommonResponse.create(AuthExecutionCode.USER_LOGIN_SUCCESS,
                authService.authenticateLogin(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiCommonResponse<AuthResponse>> register(@RequestBody @Valid UserRegistrationRequest registrationRequest) {
        logger.info("Registration request received for username: " + registrationRequest.email());

        return ApiCommonResponse.create(AuthExecutionCode.USER_REGISTRATION_SUCCESS,
                authService.registerUser(registrationRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiCommonResponse<AuthResponse>> login() {
        return ApiCommonResponse.create(AuthExecutionCode.USER_LOGOUT_SUCCESS,
                authService.logout());
    }
}
