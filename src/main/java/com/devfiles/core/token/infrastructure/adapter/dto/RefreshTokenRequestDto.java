package com.devfiles.core.token.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenRequestDto {
    @JsonProperty("refresh_token")
    @NotEmpty(message = "Refresh token is required")
    private final String token;
}
