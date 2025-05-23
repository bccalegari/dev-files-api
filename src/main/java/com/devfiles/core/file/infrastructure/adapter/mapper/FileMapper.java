package com.devfiles.core.file.infrastructure.adapter.mapper;

import com.devfiles.core.file.domain.File;
import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import com.devfiles.enterprise.domain.valueobject.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileMapper {
    private final UserMapper userMapper;

    public FileEntity toEntity(File file) {
        return FileEntity.builder()
                .id(file.getId())
                .slug(file.getSlug().getValue())
                .name(file.getName())
                .mimeType(file.getMimeType())
                .path(file.getPath())
                .size(file.getSize())
                .user(userMapper.toEntity(file.getUser()))
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .deletedAt(file.getDeletedAt())
                .embedded(file.isEmbedded())
                .build();
    }

    public File toDomain(FileEntity fileEntity) {
        return File.builder()
                .id(fileEntity.getId())
                .slug(Slug.of(fileEntity.getSlug()))
                .name(fileEntity.getName())
                .mimeType(fileEntity.getMimeType())
                .path(fileEntity.getPath())
                .size(fileEntity.getSize())
                .user(userMapper.toDomain(fileEntity.getUser()))
                .createdAt(fileEntity.getCreatedAt())
                .updatedAt(fileEntity.getUpdatedAt())
                .deletedAt(fileEntity.getDeletedAt())
                .embedded(fileEntity.isEmbedded())
                .build();
    }
}
