package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.DeleteFileStrategy;
import com.devfiles.core.file.domain.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalDeleteFileStrategy implements DeleteFileStrategy {
    @Override
    public void execute(File file) {
        if (file == null) {
            return;
        }

        try {
            var filePath = Paths.get(file.getPath());
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            log.error("Error deleting file {}", file.getSlug().getValue(), e);
        }

        try {
            var userFolder = Paths.get(file.getPath()).getParent();
            try (var stream = Files.list(userFolder)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(userFolder);
                }
            }
        } catch (Exception e) {
            log.error("Error deleting folder of user {}", file.getSlug().getValue(), e);
        }
    }
}