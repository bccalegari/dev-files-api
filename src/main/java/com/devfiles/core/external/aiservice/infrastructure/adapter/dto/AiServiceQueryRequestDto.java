package com.devfiles.core.external.aiservice.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiServiceQueryRequestDto(
        String query,
        @JsonProperty("user_slug") String userSlug,
        @JsonProperty("document_slug") String documentSlug
) {}