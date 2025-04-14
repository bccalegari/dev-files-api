package com.devfiles.core.external.aiservice.infrastructure.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@ToString
public abstract class AiServiceResponseDto<T> {
    private T data;
    private Metadata metadata;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Metadata {
        private String message;
        private String timestamp;
    }
}