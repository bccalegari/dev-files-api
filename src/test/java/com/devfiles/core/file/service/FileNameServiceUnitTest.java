package com.devfiles.core.file.service;

import com.devfiles.core.file.application.service.FileNameService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileNameServiceUnitTest {
    private final FileNameService fileNameService = new FileNameService();

    @Test
    void shouldReturnFileNameWithoutExtension() {
        String originalFileName = "file.pdf";
        String expectedFileName = "file";

        String fileName = fileNameService.execute(originalFileName);

        assertEquals(expectedFileName, fileName);
    }

    @Test
    void shouldNormalizeFileName() {
        String originalFileName = "fílè.pdf";
        String expectedFileName = "file";

        String fileName = fileNameService.execute(originalFileName);

        assertEquals(expectedFileName, fileName);
    }

    @Test
    void shouldLowerCaseFileName() {
        String originalFileName = "FILE.PDF";
        String expectedFileName = "file";

        String fileName = fileNameService.execute(originalFileName);

        assertEquals(expectedFileName, fileName);
    }

    @Test
    void shouldReplaceWhitespaceWithUnderscore() {
        String originalFileName = "file name.pdf";
        String expectedFileName = "file_name";

        String fileName = fileNameService.execute(originalFileName);

        assertEquals(expectedFileName, fileName);
    }

    @Test
    void shouldThrowExceptionWhenFileNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fileNameService.execute(null));
    }
}
