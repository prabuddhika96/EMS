package com.example.ems.application.service;

import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.dto.response.AuthResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponse authenticateLogin(AuthRequest authRequest);

    AuthResponse registerUser(@Valid UserRegistrationRequest registrationRequest);
}
