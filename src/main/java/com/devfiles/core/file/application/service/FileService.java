package com.devfiles.core.file.application.service;

import com.devfiles.core.file.abstraction.FileRepositoryGateway;
import com.devfiles.core.file.domain.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
