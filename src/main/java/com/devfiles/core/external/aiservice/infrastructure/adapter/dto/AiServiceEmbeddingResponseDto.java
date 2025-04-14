package com.devfiles.core.external.aiservice.infrastructure.adapter.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class AiServiceEmbeddingResponseDto extends AiServiceResponseDto<AiServiceEmbeddingResponseDto.Data> {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Data {
        private List<String> ids;
    }
}