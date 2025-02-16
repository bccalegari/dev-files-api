package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFileUseCase {
    private final UserService userService;
    private final FileService fileService;

    public ResponseDto<ResponseDto.Empty> execute(String userSlug, String fileSlug) {
        var user = userService.findBySlug(userSlug);

        var file = fileService.findBySlugAndUserId(fileSlug, user.getId());

        file.delete();
        fileService.save(file);

        return ResponseDto.success("File deleted successfully");
    }
}
