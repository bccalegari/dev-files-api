package com.devfiles.enterprise.infrastructure.configuration.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AwsS3ClientFactory {
    private final AwsCredentialsProviderConfiguration credentialsProvider;
    private final Environment environment;

    public S3Client execute(Region region) {
        return createS3Client(region);
    }

    public S3Client execute() {
        return createS3Client(null);
    }

    private S3Client createS3Client(Region region) {
        var regionString = environment.getProperty("aws.region");

        if (regionString == null) {
            throw new RuntimeException("AWS region not found");
        }

        var isTestProfile = environment.getActiveProfiles().length > 0
                && environment.getActiveProfiles()[0].equals("test");

        if (isTestProfile) {
            var localstackEndpoint = environment.getProperty("localstack.s3.endpoint");

            if (localstackEndpoint == null) {
                throw new RuntimeException("Localstack S3 endpoint not found");
            }

            return S3Client.builder()
                    .endpointOverride(URI.create(localstackEndpoint))
                    .region(Region.of((regionString)))
                    .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                    .build();
        }

        return S3Client.builder()
                .region(region != null ? region : Region.of(regionString))
                .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();
    }
}
