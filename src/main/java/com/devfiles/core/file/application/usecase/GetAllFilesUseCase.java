package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.file.infrastructure.adapter.dto.GetAllFilesRequestParams;
import com.devfiles.core.file.infrastructure.adapter.dto.GetAllFilesResponseDto;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFilesUseCase {
    private final UserService userService;
    private final FileService fileService;

    public ResponseDto<List<GetAllFilesResponseDto>> execute(String userSlug, GetAllFilesRequestParams requestParams) {
        var user = userService.findBySlug(userSlug);

        var pagination = requestParams.toPagination();

        var files = fileService.findAllByUserId(user.getId(), pagination);

        var getFileResponseDtoList = files.getContent().stream()
                .map(this::getAllFilesResponseDtoFromFile)
                .toList();

        return ResponseDto.success(
                getFileResponseDtoList, "Files retrieved successfully",
                files.getTotalElements(), files.getPageable().getPageNumber() + 1,
                files.getPageable().getPageSize()
        );
    }

    private GetAllFilesResponseDto getAllFilesResponseDtoFromFile(File file) {
        return GetAllFilesResponseDto.builder()
                .slug(file.getSlug().getValue())
                .name(file.getName())
                .mimeType(file.getMimeType())
                .sizeInBytes(file.getSize())
                .createdAt(file.getCreatedAt().toString())
                .build();
    }
}
