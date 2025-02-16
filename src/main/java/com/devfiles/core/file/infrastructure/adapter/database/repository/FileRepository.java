package com.devfiles.core.file.infrastructure.adapter.database.repository;

import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findBySlugAndUserIdAndDeletedAtIsNull(String slug, Long userId);

    Page<FileEntity> findAll(Specification<FileEntity> specification, Pageable pageable);

    @Query(value = """
        SELECT f
        FROM FileEntity f
        JOIN FETCH f.user
        WHERE f.deletedAt IS NOT NULL
    """)
    List<FileEntity> findAllByDeletedAtIsNotNull();

    @Modifying
    @Query(value = """
        UPDATE FileEntity f
        SET f.deletedAt = CURRENT_TIMESTAMP
        WHERE f.user.id = :userId
        AND f.deletedAt IS NULL
    """)
    void markAllFilesForRemovalByUserId(Long userId);
}