package com.devfiles.core.file.service;

import com.devfiles.AbstractTestContainersTest;
import com.devfiles.core.file.application.service.GetFileUrlService;
import com.devfiles.core.file.application.service.UploadFileService;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import com.devfiles.core.file.infrastructure.adapter.database.repository.FileRepository;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.adapter.configuration.aws.AwsCredentialsProviderConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetFileUrlServiceIntegrationTest extends AbstractTestContainersTest {
    @Autowired
    private GetFileUrlService getFileUrlService;

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

    private File uploadedFile;

    @BeforeEach
    void setUp() {
        var s3Client = S3Client.builder()
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();

        s3Client.createBucket(builder -> builder.bucket(environment.getProperty("aws.s3.bucket-name")));

        var file = new MockMultipartFile(
                "file",
                "file.pdf",
                "application/pdf",
                "test".getBytes()
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

        uploadedFile = uploadFileService.execute(file, user);

        var fileEntity = FileEntity.builder()
                .slug(uploadedFile.getSlug().getValue())
                .name(uploadedFile.getName())
                .mimeType(uploadedFile.getMimeType())
                .path(uploadedFile.getPath())
                .size(uploadedFile.getSize())
                .user(userEntity)
                .build();

        fileRepository.save(fileEntity);
    }

    @AfterEach
    void tearDown() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnPreSignedUrl() {
        var url = getFileUrlService.execute(uploadedFile.getSlug().getValue(), uploadedFile.getUser().getId());
        assertNotNull(url);
        assertThat(url).contains("https://");
        assertThat(url).contains(uploadedFile.getUser().getSlug().getValue());
        assertThat(url).contains(uploadedFile.getName());
        assertThat(url).contains("X-Amz-Signature");
    }

}