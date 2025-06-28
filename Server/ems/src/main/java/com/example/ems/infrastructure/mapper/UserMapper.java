package com.example.ems.infrastructure.mapper;

import com.example.ems.adapter.outbound.postgres.entity.UserEntity;
import com.example.ems.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User toUser(UserEntity entity){
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
