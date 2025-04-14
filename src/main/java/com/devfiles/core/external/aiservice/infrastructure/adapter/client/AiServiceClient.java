package com.devfiles.core.external.aiservice.infrastructure.adapter.client;

import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceEmbeddingRequestDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceEmbeddingResponseDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceQueryRequestDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceQueryResponseDto;
import com.devfiles.core.external.aiservice.infrastructure.configuration.AiServiceConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "aiService",
        url = "${ai.service.url}",
        path = "/api/v1",
        configuration = AiServiceConfig.class
)
public interface AiServiceClient {
    @PostMapping("/query")
    AiServiceQueryResponseDto query(@RequestBody AiServiceQueryRequestDto aiServiceQueryRequestDto);

    @PostMapping("/embedding")
    AiServiceEmbeddingResponseDto embedding(@RequestBody AiServiceEmbeddingRequestDto aiServiceEmbeddingRequestDto);
}
