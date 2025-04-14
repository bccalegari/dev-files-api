package com.devfiles.core.file.infrastructure.adapter.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QueryFileResponseDto {
    private final String answer;
}
