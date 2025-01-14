package com.devfiles.core.user.infrastructure.adapter.mapper;

import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .active(user.isActive())
                .build();
    }

    public User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .deletedAt(userEntity.getDeletedAt())
                .active(userEntity.isActive())
                .build();
    }

    public User toDomain(CreateUserRequestDto createUserRequestDto) {
        return User.builder()
                .username(createUserRequestDto.getUsername())
                .password(createUserRequestDto.getPassword())
                .email(createUserRequestDto.getEmail())
                .build();
    }
}
