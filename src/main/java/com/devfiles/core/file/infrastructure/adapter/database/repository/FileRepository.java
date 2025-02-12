package com.devfiles.core.file.infrastructure.adapter.database.repository;

import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findBySlugAndUserIdAndDeletedAtIsNull(String slug, Long userId);
}