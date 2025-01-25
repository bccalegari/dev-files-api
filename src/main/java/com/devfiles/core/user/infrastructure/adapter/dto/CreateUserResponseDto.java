package com.devfiles.core.user.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateUserResponseDto {
    private final String slug;
    private final String email;
    private final String username;
    private final boolean active;
    @JsonProperty("created_at")
    private final LocalDateTime createdAt;
}
