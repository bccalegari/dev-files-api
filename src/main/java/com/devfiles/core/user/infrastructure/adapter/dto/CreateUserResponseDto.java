package com.devfiles.core.user.infrastructure.adapter.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateUserResponseDto {
    private final Long id;
    private final String email;
    private final String username;
    private final boolean active;
    private final LocalDateTime createdAt;
}
