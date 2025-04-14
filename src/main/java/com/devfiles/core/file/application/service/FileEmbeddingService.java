package com.devfiles.core.file.application.service;

import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceEmbeddingRequestDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.gateway.AiServiceGateway;
import com.devfiles.core.file.domain.File;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEmbeddingService {
    private final AiServiceGateway aiServiceGateway;
    private final FileService fileService;

    @Transactional
    public void execute(File file) {
        if (file.isEmbedded()) {
            log.info("File already embedded: {}", file.getId());
            return;
        }

        var aiServiceEmbeddingRequestDto = getAiServiceEmbeddingRequestDto(file);
        aiServiceGateway.embedding(aiServiceEmbeddingRequestDto);
        file.embed();
        fileService.save(file);
    }

    private AiServiceEmbeddingRequestDto getAiServiceEmbeddingRequestDto(File file) {
        return new AiServiceEmbeddingRequestDto(
                file.getPath(),
                file.getUser().getSlug().getValue(),
                file.getSlug().getValue()
        );
    }
}
