package com.example.ems.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@Email String email, @NotBlank String password) { }

