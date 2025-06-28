package com.example.ems.adapter.inbound.controller;

import com.example.ems.application.service.UserService;
import com.example.ems.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @GetMapping
    public User getUser() {
        return userService.getUserById(UUID.randomUUID());
    }

    @GetMapping("/api/v1/user/profile")
//    @PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // Access authenticated user's email (username)
        String email = userDetails.getUsername();

        // Return a simple greeting message
        return "Hello, authenticated user with email: " + email;
    }
}
