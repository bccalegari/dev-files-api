package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.UploadFileStrategy;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.infrastructure.adapter.gateway.aws.s3.AwsS3Gateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudUploadFileStrategy implements UploadFileStrategy {
    private final AwsS3Gateway awsS3Gateway;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String execute(String fileName, MultipartFile file, User user) {
        var fileKey = user.getSlug().getValue() + "/" + fileName;
        awsS3Gateway.uploadFile(bucketName, fileKey, file);
        return fileKey;
    }
}