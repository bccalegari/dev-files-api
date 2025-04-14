package com.devfiles.core.file.infrastructure.adapter.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QueryFileRequestDto {
    @NotEmpty(message = "Query cannot be empty")
    private final String query;
}
