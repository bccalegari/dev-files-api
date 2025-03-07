package com.devfiles.core.file.service;

import com.devfiles.AbstractTestContainersTest;
import com.devfiles.core.file.application.service.UploadFileService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.configuration.aws.AwsCredentialsProviderConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UploadFileServiceIntegrationTest extends AbstractTestContainersTest {
    @Autowired
    private UploadFileService uploadFileService;

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
    }

    @Test
    void shouldUploadFileToS3() {
        var file = new MockMultipartFile(
                "file",
                "file.pdf",
                "application/pdf",
                "test".getBytes()
        );

        var user = User.builder()
                .id(1L)
                .slug(Slug.of("test"))
                .build();

        var uploadedFile = uploadFileService.execute(file, user);

        assertNotNull(uploadedFile);
        assertNotNull(uploadedFile.getPath());
        assertEquals("file" + "_" + uploadedFile.getSlug().getValue() + ".pdf", uploadedFile.getNameWithExtension());
        assertEquals("application/pdf", uploadedFile.getMimeType());
        assertEquals(4, uploadedFile.getSize());
        assertEquals(user.getSlug().getValue(), uploadedFile.getUser().getSlug().getValue());
        assertEquals(
                user.getSlug().getValue() + "/" + uploadedFile.getName() + ".pdf",
                uploadedFile.getPath()
        );
    }
}
