package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.application.service.GetFileUrlService;
import com.devfiles.core.file.infrastructure.adapter.dto.GetFileResponseDto;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileUseCase {
    private final UserService userService;
    private final FileService fileService;
    private final GetFileUrlService getFileUrlService;

    public ResponseDto<GetFileResponseDto> execute(String userSlug, String fileSlug) {
        var user = userService.findBySlug(userSlug);

        var file = fileService.findBySlugAndUserId(fileSlug, user.getId());

        var getFileResponseDto = GetFileResponseDto.builder()
            .slug(file.getSlug().getValue())
            .name(file.getNameWithExtension())
            .url(getFileUrlService.execute(file.getSlug().getValue(), user.getId()))
            .mimeType(file.getMimeType())
            .sizeInBytes(file.getSize())
            .createdAt(file.getCreatedAt().toString())
            .build();

        return ResponseDto.success(getFileResponseDto, "File found successfully");
    }
}
