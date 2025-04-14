package com.devfiles.core.file.infrastructure.adapter.database.entity;

import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.enterprise.infrastructure.adapter.database.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "file", name = "files")
public class FileEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(nullable = false, unique = true)
    private String path;

    @Column(nullable = false)
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private boolean embedded;
}