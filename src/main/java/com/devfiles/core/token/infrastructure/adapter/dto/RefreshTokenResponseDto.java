package com.devfiles.core.token.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenResponseDto {
    @JsonProperty("access_token")
    private final String accessToken;
}
