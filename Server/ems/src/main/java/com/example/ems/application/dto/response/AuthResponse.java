package com.example.ems.application.dto.response;

import com.example.ems.domain.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private User user;
}
