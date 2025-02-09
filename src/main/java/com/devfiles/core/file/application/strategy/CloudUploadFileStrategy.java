package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.UploadFileStrategy;
import com.devfiles.core.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudUploadFileStrategy implements UploadFileStrategy {
    @Override
    public String execute(String fileName, MultipartFile file, User user) {
        // TODO - Implement cloud storage
        throw new UnsupportedOperationException("Cloud storage not implemented yet");
    }
}