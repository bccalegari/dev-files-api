package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.UploadFileStrategy;
import com.devfiles.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class LocalUploadFileStrategy implements UploadFileStrategy {
    private static final String UPLOADS_DIR = "uploads";

    @Override
    public String execute(String fileName, MultipartFile file, User user) {
        createUploadDirDirectoryIfNotExists();

        var uploadDir = Paths.get(UPLOADS_DIR + "/" + user.getSlug().getValue()).toAbsolutePath().normalize();
        var filePath = uploadDir.resolve(fileName);

        try {
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            file.transferTo(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Please try again!", e);
        }

        return filePath.toString();
    }

    private void createUploadDirDirectoryIfNotExists() {
        var uploadDirPath = Paths.get(UPLOADS_DIR).toAbsolutePath().normalize();

        try {
            if (Files.notExists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not create the upload directory. Please try again!", e);
        }
    }
}