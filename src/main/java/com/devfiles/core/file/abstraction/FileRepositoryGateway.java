package com.devfiles.core.file.abstraction;

import com.devfiles.core.file.domain.File;

public interface FileRepositoryGateway {
    File save(File file);
}