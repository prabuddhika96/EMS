package com.example.ems.adapter.outbound.postgres.repository;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.application.repository.UserRepository;
import com.example.ems.infrastructure.constant.enums.UserRole;
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

    @Override
    public Optional<UserEntity> getUserById(UUID id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {

//        return jpaUserRepository.findByEmail(email);
        if (email.equals("prabuddhika1996@gmail.com")) {
            return Optional.of(UserEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Prabuddhika")
                    .email("prabuddhika1996@gmail.com")
                    .password(new BCryptPasswordEncoder().encode("password"))
                    .role(UserRole.USER)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build());
        }

        return Optional.empty();

    }
}
