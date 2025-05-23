package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.*;
import com.devfiles.core.file.domain.NewFileEvent;
import com.devfiles.core.file.infrastructure.adapter.dto.CreateFileResponseDto;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CreateFileUseCase {
    private final UserService userService;
    private final FileValidatorService fileValidatorService;
    private final UploadFileService uploadFileService;
    private final FileService fileService;
    private final GetFileUrlService getFileUrlService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public ResponseDto<CreateFileResponseDto> execute(String slug, MultipartFile file) {
        var user = userService.findBySlug(slug);

        fileValidatorService.execute(file);

        var fileDomain = uploadFileService.execute(file, user);

        fileDomain = fileService.save(fileDomain);

        var createFileResponseDto = CreateFileResponseDto.builder()
                .slug(fileDomain.getSlug().getValue())
                .fileName(fileDomain.getNameWithExtension())
                .url(getFileUrlService.execute(fileDomain.getSlug().getValue(), user.getId()))
                .mimeType(fileDomain.getMimeType())
                .sizeInBytes(fileDomain.getSize())
                .createdAt(fileDomain.getCreatedAt())
                .build();

        applicationEventPublisher.publishEvent(new NewFileEvent(fileDomain));

        return ResponseDto.success(createFileResponseDto, "File created successfully");
    }
}
