package com.devfiles.core.external.aiservice.infrastructure.adapter.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class AiServiceQueryResponseDto extends AiServiceResponseDto<AiServiceQueryResponseDto.Data> {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Data {
        private String response;
    }
}