package com.example.ems.adapter.inbound.controller;

import com.example.ems.adapter.inbound.util.ApiCommonResponse;
import com.example.ems.application.service.UserService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.executioncode.UserExecutionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @GetMapping("/user-list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiCommonResponse<Page<User>>> getUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ApiCommonResponse.create(UserExecutionCode.USER_LIST_FOUND,userService.getUserList(pageable));
    }

    @PatchMapping("/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiCommonResponse<String>> changeUserRole(
            @RequestParam UUID userId,
            @RequestParam String newRole
    ) {
        return ApiCommonResponse.create(UserExecutionCode.USER_ROLE_CHANGED_SUCCESS, userService.changeUserRole(userId, newRole));
    }

//    @GetMapping("/api/v1/user/profile")
//    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
//        // Access authenticated user's email (username)
//        String email = userDetails.getUsername();
//
//        // Return a simple greeting message
//        return "Hello, authenticated user with email: " + email;
//    }
}
