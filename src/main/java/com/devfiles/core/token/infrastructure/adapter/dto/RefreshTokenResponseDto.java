package com.devfiles.core.token.infrastructure.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenResponseDto {
    private final String token;
}
