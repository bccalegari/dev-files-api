package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.DeleteFileStrategy;
import com.devfiles.core.file.domain.File;
import com.devfiles.enterprise.infrastructure.adapter.gateway.aws.s3.AwsS3Gateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudDeleteFileStrategy implements DeleteFileStrategy {
    private final AwsS3Gateway awsS3Gateway;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public void execute(File file) {
        var fileKey = file.getUser().getSlug().getValue() + "/" + file.getNameWithExtension();
        awsS3Gateway.deleteFile(bucketName, fileKey);
    }
}