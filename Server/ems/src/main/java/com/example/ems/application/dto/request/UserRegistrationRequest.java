package com.example.ems.application.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password
) {
    
}
