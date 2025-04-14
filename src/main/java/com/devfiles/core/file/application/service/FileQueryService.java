package com.devfiles.core.file.application.service;

import com.devfiles.core.external.aiservice.infrastructure.adapter.dto.AiServiceQueryRequestDto;
import com.devfiles.core.external.aiservice.infrastructure.adapter.gateway.AiServiceGateway;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileQueryService {
    private final AiServiceGateway aiServiceGateway;

    public String execute(String query, File file) {
        var aiServiceQueryRequestDto = getAiServiceQueryRequestDto(query, file.getUser(), file);
        return aiServiceGateway.query(aiServiceQueryRequestDto);
    }

    private AiServiceQueryRequestDto getAiServiceQueryRequestDto(String query, User user, File file) {
        return new AiServiceQueryRequestDto(
                query,
                user.getSlug().getValue(),
                file.getSlug().getValue()
        );
    }
}
