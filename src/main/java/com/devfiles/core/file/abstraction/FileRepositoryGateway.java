package com.devfiles.core.file.abstraction;

import com.devfiles.core.file.domain.File;
import com.devfiles.enterprise.infrastructure.adapter.database.Pagination;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FileRepositoryGateway {
    File save(File file);
    Optional<File> findBySlugAndUserId(String slug, Long userId);
    Page<File> findAllByUserId(Long userId, Pagination pagination);
    List<File> findAllFilesMarkedForRemoval();
    void markAllFilesForRemovalByUserId(Long userId);
    void hardDeleteAll(List<File> files);
}