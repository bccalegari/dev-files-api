package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.infrastructure.adapter.dto.LocalGetFileResourceResponseDto;
import com.devfiles.core.user.application.exception.UserNotAllowedToManageAnotherUserResourcesException;
import com.devfiles.core.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Profile("local")
public class LocalGetFileResourceUseCase {
    private final UserService userService;
    private final FileService fileService;

    public LocalGetFileResourceResponseDto execute(String userSlug, String fileSlug) {
        var user = userService.findBySlug(userSlug);

        var file = fileService.findBySlugAndUserId(fileSlug, user.getId());

        try {
            var filePath = Paths.get(file.getPath()).normalize().toAbsolutePath();
            var resource = new UrlResource(filePath.toUri());
            return LocalGetFileResourceResponseDto.builder()
                    .fileNameWithExtension(file.getNameWithExtension())
                    .mimeType(file.getMimeType())
                    .urlResource(resource)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error getting file URL", e);
        }
    }
}
