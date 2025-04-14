package com.devfiles.core.external.aiservice.infrastructure.adapter.gateway;

import com.devfiles.core.external.aiservice.application.exception.AiServiceUnavailableException;
import com.devfiles.core.external.aiservice.infrastructure.adapter.client.AiServiceClient;
import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceEmbeddingRequestDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceQueryRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiServiceGateway {
    private final AiServiceClient aiServiceClient;

    @CircuitBreaker(
            name = "aiService",
            fallbackMethod = "queryFallback"
    )
    @Retry(name = "aiService")
    @RateLimiter(name = "aiService")
    public String query(@RequestBody AiServiceQueryRequestDto aiServiceQueryRequestDto) {
        log.info("Calling AI Service for query: {}", aiServiceQueryRequestDto);

        var response = aiServiceClient.query(aiServiceQueryRequestDto);

        log.info("Query AI Service response: {}", response);

        return response.getData().getResponse();
    }

    private String queryFallback(AiServiceQueryRequestDto aiServiceQueryRequestDto, Throwable throwable) {
        log.error("Tried to call AI Service for query, but it failed, service is unavailable.", throwable);
        throw new AiServiceUnavailableException();
    }

    @CircuitBreaker(
            name = "aiService",
            fallbackMethod = "embeddingFallback"
    )
    @Retry(name = "aiService")
    @RateLimiter(name = "aiService")
    public void embedding(@RequestBody AiServiceEmbeddingRequestDto aiServiceEmbeddingRequestDto) {
        log.info("Calling AI Service for embedding: {}", aiServiceEmbeddingRequestDto);

        var response = aiServiceClient.embedding(aiServiceEmbeddingRequestDto);

        log.info("Embedding AI Service response: {}", response);
    }

    private void embeddingFallback(AiServiceEmbeddingRequestDto aiServiceEmbeddingRequestDto, Throwable throwable) {
        log.error("Tried to call AI Service for embedding, but it failed, service is unavailable.", throwable);
        throw new AiServiceUnavailableException();
    }
}
