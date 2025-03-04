package com.devfiles.core.file.service;

import com.devfiles.core.file.application.exception.FileCorruptedException;
import com.devfiles.core.file.application.exception.FileMimeTypeNotAllowedException;
import com.devfiles.core.file.application.exception.FileSizeNotAllowedException;
import com.devfiles.core.file.application.service.FileValidatorService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileValidatorServiceUnitTest {
    private final FileValidatorService fileValidatorService = new FileValidatorService();

    @Test
    void shouldThrowExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fileValidatorService.execute(null));
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> fileValidatorService.execute(null));
    }

    @Test
    void shouldThrowExceptionWhenFileIsCorrupted() {
        var file = new MockMultipartFile(
            "file",
            "file.pdf",
            "application/pdf",
            new byte[1]
        ) {
            @Override
            public byte @NotNull [] getBytes() throws IOException {
                throw new IOException("");
            }
        };

        assertThrows(FileCorruptedException.class, () -> fileValidatorService.execute(file));
    }

    @Test
    void shouldThrowExceptionWhenFileMimeTypeIsNotAllowed() {
        var file = new MockMultipartFile(
            "file",
            "file.xml",
            "application/xml",
            new byte[1]
        );

        assertThrows(FileMimeTypeNotAllowedException.class, () -> fileValidatorService.execute(file));
    }

    @Test
    void shouldThrowExceptionWhenFileSizeIsNotAllowed() {
        var file = new MockMultipartFile(
            "file",
            "file.pdf",
            "application/pdf",
            new byte[52428801]
        );

        assertThrows(FileSizeNotAllowedException.class, () -> fileValidatorService.execute(file));
    }
}
