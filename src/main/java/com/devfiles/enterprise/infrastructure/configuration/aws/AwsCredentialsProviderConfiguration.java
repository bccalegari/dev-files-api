package com.devfiles.enterprise.infrastructure.configuration.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
@RequiredArgsConstructor
public class AwsCredentialsProviderConfiguration {
    private final Environment environment;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        final String accessKey = environment.getProperty("aws.credentials.access-key");
        final String secretKey = environment.getProperty("aws.credentials.secret-key");

        if (accessKey == null || secretKey == null) {
            throw new RuntimeException("AWS credentials not found");
        }

        try {
            var credentials = AwsBasicCredentials.create(accessKey, secretKey);
            return StaticCredentialsProvider.create(credentials);
        } catch (Exception e) {
            throw new RuntimeException("Error setting AWS credentials", e);
        }
    }
}
