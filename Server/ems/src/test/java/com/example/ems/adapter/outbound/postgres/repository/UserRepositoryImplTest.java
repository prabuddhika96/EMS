package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.UserRole;
import com.example.ems.infrastructure.exceptions.UserException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest {

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private SpringDataUserRepository jpaUserRepository;

    @Mock
    private LoggingUtil logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_success() {
        UUID userId = UUID.randomUUID();
        UserEntity mockEntity = UserEntity.builder().id(userId).name("Test User").email("testuser@gmai.com").build();
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(mockEntity));

        User user = userRepository.getUserById(userId);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    void testGetUserById_notFound() {
        UUID userId = UUID.randomUUID();
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userRepository.getUserById(userId));
    }

    @Test
    void testGetUserByEmail_success() {
        String email = "testuser@gmai.com";
        UserEntity entity = UserEntity.builder().email(email).build();
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        Optional<UserEntity> result = userRepository.getUserByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void testSave_success() {
        UserRegistrationRequest request = new UserRegistrationRequest("User", "testuser@gmai.com", "password123");
        UserEntity saved = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("User")
                .email("testuser@gmai.com")
                .role(UserRole.USER)
                .build();

        when(jpaUserRepository.save(any())).thenReturn(saved);

        User user = userRepository.save(request);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("testuser@gmai.com");
    }

    @Test
    void testGetUserList_success() {
        UserEntity userEntity = UserEntity.builder().id(UUID.randomUUID()).email("testuser@gmail.com").name("Test").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> userPage = new PageImpl<>(List.of(userEntity));

        when(jpaUserRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userRepository.getUserList(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test");
    }

    @Test
    void testChangeUserRole_success() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
                .id(userId)
                .role(UserRole.USER)
                .build();

        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = userRepository.changeUserRole(userId, "ADMIN");

        assertThat(result).contains("User role changed to ADMIN");
        verify(jpaUserRepository).save(user);
    }

    @Test
    void testChangeUserRole_invalidRole() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().id(userId).build();
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () -> userRepository.changeUserRole(userId, "INVALID_ROLE"));
    }

    @Test
    void testChangeUserRole_userNotFound() {
        UUID userId = UUID.randomUUID();
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userRepository.changeUserRole(userId, "ADMIN"));
    }

}
