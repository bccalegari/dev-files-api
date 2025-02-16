package com.devfiles.core.file.infrastructure.adapter.gateway;

import com.devfiles.core.file.abstraction.FileRepositoryGateway;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.file.infrastructure.adapter.database.repository.FileRepository;
import com.devfiles.core.file.infrastructure.adapter.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Qualifier("fileJpaRepositoryGateway")
public class FileJpaRepositoryGateway implements FileRepositoryGateway {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Override
    public File save(File file) {
        var fileEntity = fileMapper.toEntity(file);
        fileEntity = fileRepository.saveAndFlush(fileEntity);
        return fileMapper.toDomain(fileEntity);
    }

    @Override
    public Optional<File> findBySlugAndUserId(String slug, Long userId) {
        return fileRepository.findBySlugAndUserIdAndDeletedAtIsNull(slug, userId)
                .map(fileMapper::toDomain);
    }

    @Override
    public List<File> findAllFilesMarkedForRemoval() {
        return fileRepository.findAllByDeletedAtIsNotNull().stream()
                .map(fileMapper::toDomain)
                .toList();
    }

    @Override
    public void markAllFilesForRemovalByUserId(Long userId) {
        fileRepository.markAllFilesForRemovalByUserId(userId);
    }
}
