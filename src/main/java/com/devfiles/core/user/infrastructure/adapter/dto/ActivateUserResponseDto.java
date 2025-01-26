package com.devfiles.core.user.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ActivateUserResponseDto {
    private final String slug;
    private final String email;
    private final boolean active;
    @JsonProperty("updated_at")
    private final LocalDateTime updatedAt;
}
