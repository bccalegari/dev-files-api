package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.GetFileUrlStrategy;
import com.devfiles.core.file.domain.File;
import com.devfiles.enterprise.infrastructure.adapter.gateway.aws.s3.AwsS3Gateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CloudGetFileUrlStrategy implements GetFileUrlStrategy {
    private final AwsS3Gateway awsS3Gateway;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String execute(File file) {
        var fileKey = file.getUser().getSlug().getValue() + "/" + file.getNameWithExtension();
        return awsS3Gateway.getPreSignedUrl(bucketName, fileKey, Duration.ofMinutes(30));
    }
}