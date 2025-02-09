package com.devfiles.core.file.application.service;

import com.devfiles.core.file.application.exception.FileCorruptedException;
import com.devfiles.core.file.application.exception.FileMimeTypeNotAllowedException;
import com.devfiles.core.file.application.exception.FileSizeNotAllowedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileValidatorService {
    private static final String[] ALLOWED_FILE_MIME_TYPES = {
        "image/jpeg",
        "image/png",
        "application/pdf"
    };

    private static final Long MAX_FILE_SIZE_IN_BYTES = 52428800L; // 50MB

    public void execute(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("File is required");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        validateFile(file);
    }

    private void validateFile(MultipartFile file) {
        if (isFileCorrupted(file)) {
            throw new FileCorruptedException();
        }

        if (!isMimeTypeValid(file)) {
            throw new FileMimeTypeNotAllowedException();
        }

        if (!isFileSizeValid(file)) {
            throw new FileSizeNotAllowedException();
        }
    }

    private boolean isFileCorrupted(MultipartFile file) {
        try {
            file.getBytes();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private boolean isMimeTypeValid(MultipartFile file) {
        String mimeType = file.getContentType();
        for (String allowedMimeType : ALLOWED_FILE_MIME_TYPES) {
            if (allowedMimeType.equals(mimeType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFileSizeValid(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE_IN_BYTES;
    }
}
