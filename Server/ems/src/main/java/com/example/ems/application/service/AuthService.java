package com.example.ems.application.service;

import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticateLogin(AuthRequest authRequest);
}
