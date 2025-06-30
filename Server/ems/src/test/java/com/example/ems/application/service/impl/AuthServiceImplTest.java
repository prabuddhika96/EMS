package com.example.ems.application.service.impl;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.AuthRequest;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.dto.response.AuthResponse;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.UserRole;
import com.example.ems.infrastructure.exceptions.CommonException;
import com.example.ems.infrastructure.security.jwt.JwtTokenProvider;
import com.example.ems.infrastructure.utli.AuthUtil;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private LoggingUtil logger;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock private HttpServletResponse httpServletResponse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(authService, "jwtExpirationMs", 3600000);
    }

    @Test
    void testAuthenticateLogin_Success() {
        String email = "testuser@gmail.com";
        String password = "password";

        AuthRequest authRequest = new AuthRequest(email, password);

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        UserEntity mockEntity = UserEntity.builder().id(UUID.randomUUID()).name("Test User").email("testuser@gmail.com").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(email);
        when(jwtTokenProvider.generateToken(userDetails)).thenReturn("jwt-token");
        when(authUtil.getCurrentHttpResponse()).thenReturn(httpServletResponse);
        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(mockEntity));

        AuthResponse response = authService.authenticateLogin(authRequest);

        assertNotNull(response);
        assertNotNull(response.getUser());
        assertEquals(email, response.getUser().getEmail());
        verify(httpServletResponse).addHeader(eq("Set-Cookie"), contains("accessToken"));
    }

    @Test
    void testRegisterUser_Success() {
        String email = "newuser@gmail.com";
        String password = "password";
        String name = "New User";
        UUID userId = UUID.randomUUID();
        UserRegistrationRequest request = new UserRegistrationRequest(name, email, password);

        User savedUser = new User(userId, name, email, UserRole.USER, null, null);

        when(userRepository.getUserByEmail(email))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(
                        UserEntity.builder()
                                .id(userId)
                                .name(name)
                                .email(email)
                                .role(UserRole.USER)
                                .build()
                ));

        when(userRepository.save(request)).thenReturn(savedUser);

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(email);
        when(jwtTokenProvider.generateToken(any())).thenReturn("mock-jwt-token");
        when(authUtil.getCurrentHttpResponse()).thenReturn(httpServletResponse);

        AuthResponse response = authService.registerUser(request);

        assertNotNull(response);
        assertNotNull(response.getUser());
        assertEquals(email, response.getUser().getEmail());
        assertEquals(name, response.getUser().getName());
    }

    @Test
    void testRegisterUser_AlreadyExists() {
        String email = "existinguser@gmail.com";
        String password = "password";
        UserRegistrationRequest request = new UserRegistrationRequest("User", email, password);

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(CommonException.class, () -> authService.registerUser(request));
    }

    @Test
    void testLogout() {
        when(authUtil.getCurrentUsername()).thenReturn("testuser@gmail.com");
        when(authUtil.getCurrentHttpResponse()).thenReturn(httpServletResponse);

        AuthResponse response = authService.logout();

        assertNull(response.getUser());
        verify(httpServletResponse).addHeader(eq("Set-Cookie"), contains("accessToken="));
    }
}
