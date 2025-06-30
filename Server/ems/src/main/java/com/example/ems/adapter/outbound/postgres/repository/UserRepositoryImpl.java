package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.dto.request.UserRegistrationRequest;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.domain.model.User;
import com.example.ems.infrastructure.constant.enums.UserRole;
import com.example.ems.infrastructure.constant.executioncode.CommonExecutionCode;
import com.example.ems.infrastructure.constant.executioncode.UserExecutionCode;
import com.example.ems.infrastructure.exceptions.CommonException;
import com.example.ems.infrastructure.exceptions.UserException;
import com.example.ems.infrastructure.mapper.UserMapper;
import com.example.ems.infrastructure.utli.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.example.ems.infrastructure.mapper.UserMapper.toUser;

interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository jpaUserRepository;
    private final LoggingUtil logger;

    @Override
    public User getUserById(UUID id) {

        try {
            UserEntity userEntity = jpaUserRepository.findById(id).orElseThrow(
                    () -> new UserException(UserExecutionCode.USER_NOT_FOUND)
            );
            return toUser(userEntity);
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user by email: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG);
        }
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
    public User save(UserRegistrationRequest userRegistrationRequest) {
        try{
            UserEntity userEntity = UserEntity.builder()
                    .name(userRegistrationRequest.name())
                    .email(userRegistrationRequest.email())
                    .password(new BCryptPasswordEncoder().encode(userRegistrationRequest.password()))
                    .role(UserRole.USER)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            return toUser(jpaUserRepository.save(userEntity));
        } catch (Exception e) {
            logger.error("Error occurred while saving user: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG );
        }
    }

    @Override
    public Page<User> getUserList(Pageable pageable) {
        try{
            Page<UserEntity> userEntityPage = jpaUserRepository.findAll(pageable);
            return userEntityPage.map(UserMapper::toUser);
        } catch (UserException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Error occurred while saving user: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG );
        }

    }

    @Override
    public String changeUserRole(UUID userId, String newRole) {
        try {
            UserEntity userEntity = jpaUserRepository.findById(userId)
                    .orElseThrow(() -> new UserException(UserExecutionCode.USER_NOT_FOUND));

            UserRole roleEnum;
            try {
                roleEnum = UserRole.valueOf(newRole.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new UserException(UserExecutionCode.INVALID_ROLE);
            }

            userEntity.setId(userId);
            userEntity.setRole(roleEnum);
            userEntity.setUpdatedAt(Instant.now());

            jpaUserRepository.save(userEntity);

            return "User role changed to " + newRole;
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while changing user role: " + e.getMessage());
            throw new CommonException(CommonExecutionCode.SOMETHING_WENT_WRONG);
        }
    }

}
