package com.devfiles.enterprise.infrastructure.configuration.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
@RequiredArgsConstructor
public class AwsS3PresignerFactory {
    private final AwsCredentialsProviderConfiguration credentialsProvider;
    private final Environment environment;

    public S3Presigner execute(Region region) {
        return createS3Presigner(region);
    }

    public S3Presigner execute() {
        return createS3Presigner(null);
    }

    private S3Presigner createS3Presigner(Region region) {
        var regionString = environment.getProperty("aws.region");

        if (regionString == null) {
            throw new RuntimeException("AWS region not found");
        }

        return S3Presigner.builder()
                .region(region != null ? region : Region.of(regionString))
                .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();
    }
}
