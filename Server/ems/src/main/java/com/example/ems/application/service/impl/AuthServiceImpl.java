package com.example.ems.application.service.impl;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.dto.response.AuthResponse;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.application.service.AuthService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.executioncode.UserExecutionCode;
import com.example.ems.infrastructure.exceptions.CommonException;
import com.example.ems.infrastructure.exceptions.UserException;
import com.example.ems.infrastructure.utli.AuthUtil;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.ems.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final LoggingUtil logger;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthUtil authUtil;

    @Override
    public AuthResponse authenticateLogin(AuthRequest authRequest) {
        logger.info("Authenticating user with email: " + authRequest.email());
        try{
            return login(authRequest.email(), authRequest.password());
        }catch (Exception e) {
            logger.error("Authentication failed for user with email: " + authRequest.email() + " :: "+ e);
            throw new CommonException(UserExecutionCode.USER_NOT_FOUND);
        }
    }

    private AuthResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        logger.info("User authenticated successfully: " + email);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userDetails);

        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();

        authUtil.getCurrentHttpResponse().addHeader("Set-Cookie", jwtCookie.toString());

        UserEntity userEntity = userRepository.getUserByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserException(UserExecutionCode.USER_NOT_FOUND)
        );

        User user= User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .role(userEntity.getRole())
                .build();

        return AuthResponse.builder()
//                .token(jwt)
                .user(user)
                .build();
    }

    @Override
    public AuthResponse registerUser(UserRegistrationRequest registrationRequest) {
        logger.info("Registering user with email: " + registrationRequest.email());

        Optional<UserEntity> user = userRepository.getUserByEmail(registrationRequest.email());

        if (user.isPresent()) {
            logger.error("User with email already exists: " + registrationRequest.email());
            throw new CommonException(UserExecutionCode.USER_ALREADY_EXISTS);
        }

        User savedUser = userRepository.save(registrationRequest);

        if(savedUser == null) {
            logger.error("Failed to register user with email: " + registrationRequest.email());
            throw new CommonException(UserExecutionCode.USER_REGISTRATION_FAILED);
        }

        logger.info("User registered successfully: " + registrationRequest.email());

        return login(registrationRequest.email(), registrationRequest.password());
    }
}
