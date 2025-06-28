package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.infrastructure.constant.enums.UserRole;
import com.example.ems.infrastructure.constant.executioncode.CommonExecutionCode;
import com.example.ems.infrastructure.exceptions.CommonException;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository jpaUserRepository;
    private final LoggingUtil logger;

    @Override
    public Optional<UserEntity> getUserById(UUID id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        try {
            return jpaUserRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Error occurred while fetching user by email: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public UserEntity save(UserRegistrationRequest userRegistrationRequest) {
        try{
            UserEntity userEntity = UserEntity.builder()
//                    .id(UUID.randomUUID())
                    .name(userRegistrationRequest.name())
                    .email(userRegistrationRequest.email())
                    .password(new BCryptPasswordEncoder().encode(userRegistrationRequest.password()))
                    .role(UserRole.USER)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            return jpaUserRepository.save(userEntity);
        } catch (Exception e) {
            logger.error("Error occurred while saving user: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG );
        }
    }
}
