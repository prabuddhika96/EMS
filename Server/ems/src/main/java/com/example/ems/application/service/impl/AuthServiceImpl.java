package com.example.ems.application.service.impl;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.response.AuthResponse;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.application.service.AuthService;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.executioncode.UserExecutionCode;
import com.example.ems.infrastructure.exceptions.CommonException;
import com.example.ems.infrastructure.exceptions.UserException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.ems.infrastructure.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final LoggingUtil logger;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse authenticateLogin(AuthRequest authRequest) {
        logger.info("Authenticating user with email: " + authRequest.email());
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
            );

            logger.info("User authenticated successfully: " + authRequest.email());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateToken(userDetails);

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
                    .token(jwt)
                    .user(user)
                    .build();
        }catch (Exception e) {
            logger.error("Authentication failed for user with email: " + authRequest.email() + " :: "+ e);
            throw new CommonException(UserExecutionCode.USER_NOT_FOUND);
        }
    }
}
