package com.devfiles.core.file.abstraction;

import com.devfiles.core.file.domain.File;

public interface GetFileUrlStrategy {
    String execute(File file);
}
