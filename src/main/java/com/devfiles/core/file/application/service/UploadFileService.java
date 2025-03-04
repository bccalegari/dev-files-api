package com.devfiles.core.file.application.service;

import com.devfiles.core.file.application.factory.UploadFileStrategyFactory;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileStrategyFactory uploadFileStrategyFactory;
    private final FileNameService fileNameService;

    public File execute(MultipartFile file, User user) {
        var uploadFileStrategy = uploadFileStrategyFactory.getStrategy();

        var fileNameParts = getFileParts(file);
        var fileName = fileNameParts[0];
        var fileExtension = fileNameParts[1];

        var normalizedFileName = fileNameService.execute(fileName);
        var fileSlug = new Slug();
        var fileNameWithId = normalizedFileName + "_" + fileSlug.getValue();

        String path = uploadFileStrategy.execute(fileNameWithId.concat(fileExtension), file, user);

        return File.builder()
                .slug(fileSlug)
                .name(fileNameWithId)
                .mimeType(file.getContentType())
                .path(path)
                .size(file.getSize())
                .user(user)
                .build();
    }

    public String[] getFileParts(MultipartFile file) {
        var lastDotIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        return new String[] {
            file.getOriginalFilename().substring(0, lastDotIndex),
            file.getOriginalFilename().substring(lastDotIndex)
        };
    }
}
