package com.devfiles.core.file.application.service;

import com.devfiles.core.file.abstraction.FileRepositoryGateway;
import com.devfiles.core.file.application.exception.FileNotFoundException;
import com.devfiles.core.file.domain.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileRepositoryGateway fileRepositoryGateway;

    public FileService(
            @Qualifier("fileJpaRepositoryGateway") FileRepositoryGateway fileRepositoryGateway
    ) {
        this.fileRepositoryGateway = fileRepositoryGateway;
    }

    public File save(File file) {
        return fileRepositoryGateway.save(file);
    }

    public File findBySlugAndUserId(String slug, Long userId) {
        var fileOp = fileRepositoryGateway.findBySlugAndUserId(slug, userId);

        if (fileOp.isEmpty()) {
            throw new FileNotFoundException();
        }

        return fileOp.get();
    }

    public List<File> findAllFilesMarkedForRemoval() {
        return fileRepositoryGateway.findAllFilesMarkedForRemoval();
    }

    public void markAllFilesForRemovalByUserId(Long userId) {
        fileRepositoryGateway.markAllFilesForRemovalByUserId(userId);
    }
}
