package com.devfiles.core.file.abstraction;

import com.devfiles.core.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileStrategy {
    String execute(String fileName, MultipartFile file, User user);
}
