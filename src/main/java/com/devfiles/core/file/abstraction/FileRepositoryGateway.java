package com.devfiles.core.file.abstraction;

import com.devfiles.core.file.domain.File;

import java.util.Optional;

public interface FileRepositoryGateway {
    File save(File file);
    Optional<File> findBySlugAndUserId(String slug, Long userId);
}