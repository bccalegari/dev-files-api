package com.devfiles.enterprise.infrastructure.adapter.gateway.aws.s3;

import com.devfiles.enterprise.infrastructure.adapter.configuration.aws.AwsS3ClientFactory;
import com.devfiles.enterprise.infrastructure.adapter.configuration.aws.AwsS3PresignerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!local")
public class AwsS3Gateway {
    private final AwsS3ClientFactory s3ClientFactory;
    private final AwsS3PresignerFactory s3PresignerFactory;

    public void uploadFile(String bucket, String key, MultipartFile file) {
        S3Client s3Client = createS3Client();
        putFile(s3Client, bucket, key, file);
    }

    public String getPreSignedUrl(String bucket, String key, Duration duration) {
        var s3Presigner = s3PresignerFactory.execute();
        return getPreSignedUrl(s3Presigner, bucket, key, duration);
    }

    public void deleteFile(String bucket, String key) {
        S3Client s3Client = createS3Client();
        deleteFile(s3Client, bucket, key);
    }

    private void putFile(S3Client s3Client, String bucket, String key, MultipartFile file) {
        try {
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (S3Exception s3e) {
            log.error("S3 specific error uploading file: {}", s3e.awsErrorDetails().errorMessage(), s3e);
            throw s3e;
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String getPreSignedUrl(S3Presigner presigner, String bucket, String key, Duration duration) {
        try {
            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
                    builder -> builder.signatureDuration(duration == null ? Duration.ofMinutes(30) : duration)
                            .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(key).build())
            );

            return presignedGetObjectRequest.url().toString();
        } catch (S3Exception s3e) {
            log.error("S3 specific error generating pre-signed URL: {}", s3e.awsErrorDetails().errorMessage(), s3e);
            throw s3e;
        } catch (Exception e) {
            log.error("Error generating pre-signed URL: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void deleteFile(S3Client s3Client, String bucket, String key) {
        try {
            s3Client.deleteObject(deleteObjectRequest -> deleteObjectRequest.bucket(bucket).key(key));
            log.info("File deleted successfully: {}/{}", bucket, key);
        } catch (S3Exception s3e) {
            log.error("S3 specific error deleting file: {}", s3e.awsErrorDetails().errorMessage(), s3e);
            throw s3e;
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
            throw e;
        }
    }

    private S3Client createS3Client() {
        try {
            return s3ClientFactory.execute();
        } catch (Exception e) {
            log.error("Error creating S3 client: {}", e.getMessage(), e);
            throw e;
        }
    }
}
