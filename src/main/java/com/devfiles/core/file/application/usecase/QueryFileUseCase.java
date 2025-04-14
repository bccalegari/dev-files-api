package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileEmbeddingService;
import com.devfiles.core.file.application.service.FileQueryService;
import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.infrastructure.adapter.dto.QueryFileResponseDto;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryFileUseCase {
    private final UserService userService;
    private final FileService fileService;
    private final FileEmbeddingService fileEmbeddingService;
    private final FileQueryService fileQueryService;

    public ResponseDto<QueryFileResponseDto> execute(String query, String userSlug, String fileSlug) {
        var user = userService.findBySlug(userSlug);

        var file = fileService.findBySlugAndUserId(fileSlug, user.getId());

        if (!file.isEmbedded()) {
            fileEmbeddingService.execute(file);
        }

        var queryResponse = fileQueryService.execute(query, file);

        var queryFileResponseDto = QueryFileResponseDto.builder()
                .answer(queryResponse)
                .build();

        return ResponseDto.success(queryFileResponseDto, "Answer retrieved successfully");
    }
}
