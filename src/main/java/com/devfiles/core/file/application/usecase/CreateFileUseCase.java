package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.application.service.FileValidatorService;
import com.devfiles.core.file.application.service.UploadFileService;
import com.devfiles.core.file.infrastructure.adapter.dto.CreateFileResponseDto;
import com.devfiles.core.user.application.exception.UserNotAllowedToManageAnotherUserResourcesException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CreateFileUseCase {
    private final UserService userService;
    private final FileValidatorService fileValidatorService;
    private final UploadFileService uploadFileService;
    private final FileService fileService;

    @Transactional
    public ResponseDto<CreateFileResponseDto> execute(String loggedInUserSlug, String slug, MultipartFile file) {
        if (!userService.isUserAllowedToManageAnotherUserResources(loggedInUserSlug, slug)) {
            throw new UserNotAllowedToManageAnotherUserResourcesException();
        }

        var user = userService.findBySlug(slug);

        fileValidatorService.execute(file);

        var fileDomain = uploadFileService.execute(file, user);

        fileDomain = fileService.save(fileDomain);

        var createFileResponseDto = CreateFileResponseDto.builder()
                .slug(fileDomain.getSlug().getValue())
                .fileName(fileDomain.getNameWithExtension())
                .mimeType(fileDomain.getMimeType())
                .size(fileDomain.getSize())
                .createdAt(fileDomain.getCreatedAt())
                .build();

        return ResponseDto.success(createFileResponseDto, "File created successfully");
    }
}
