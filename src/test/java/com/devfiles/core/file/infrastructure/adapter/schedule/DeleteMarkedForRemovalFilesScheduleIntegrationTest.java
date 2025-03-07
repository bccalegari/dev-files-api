package com.devfiles.core.file.infrastructure.adapter.schedule;

import com.devfiles.AbstractTestContainersTest;
import com.devfiles.core.file.application.service.UploadFileService;
import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import com.devfiles.core.file.infrastructure.adapter.database.repository.FileRepository;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.configuration.aws.AwsCredentialsProviderConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDateTime;
import java.util.List;

public class DeleteMarkedForRemovalFilesScheduleIntegrationTest extends AbstractTestContainersTest {
    @Autowired
    private DeleteMarkedForRemovalFilesSchedule deleteMarkedForRemovalFilesSchedule;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AwsCredentialsProviderConfiguration credentialsProvider;

    @Autowired
    private Environment environment;

    @BeforeEach
    void setUp() {
        var s3Client = S3Client.builder()
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();

        s3Client.createBucket(builder -> builder.bucket(environment.getProperty("aws.s3.bucket-name")));

        var file1 = new MockMultipartFile(
                "file",
                "file.pdf",
                "application/pdf",
                "test".getBytes()
        );

        var file2 = new MockMultipartFile(
                "file2",
                "file2.pdf",
                "application/pdf",
                "test2".getBytes()
        );

        var user = User.builder()
                .id(1L)
                .slug(Slug.of("test"))
                .email("test@test.com")
                .username("test")
                .password("test")
                .createdAt(LocalDateTime.now())
                .build();

        var userEntity = UserEntity.builder()
                .slug(user.getSlug().getValue())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .build();

        userRepository.save(userEntity);

        var uploadedFile = uploadFileService.execute(file1, user);
        var uploadedFile2 = uploadFileService.execute(file2, user);

        var fileEntity1 = FileEntity.builder()
                .slug(uploadedFile.getSlug().getValue())
                .name(uploadedFile.getName())
                .mimeType(uploadedFile.getMimeType())
                .path(uploadedFile.getPath())
                .size(uploadedFile.getSize())
                .user(userEntity)
                .deletedAt(LocalDateTime.now())
                .build();

        var fileEntity2 = FileEntity.builder()
                .slug(uploadedFile2.getSlug().getValue())
                .name(uploadedFile2.getName())
                .mimeType(uploadedFile2.getMimeType())
                .path(uploadedFile2.getPath())
                .size(uploadedFile2.getSize())
                .user(userEntity)
                .deletedAt(LocalDateTime.now())
                .build();

        fileRepository.saveAll(List.of(fileEntity1, fileEntity2));
    }

    @AfterEach
    void tearDown() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldDeleteFilesMarkedForRemoval() {
        deleteMarkedForRemovalFilesSchedule.execute();
    }
}