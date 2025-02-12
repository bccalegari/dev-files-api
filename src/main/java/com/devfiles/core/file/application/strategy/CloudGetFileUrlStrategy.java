package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.GetFileUrlStrategy;
import com.devfiles.core.file.domain.File;
import org.springframework.stereotype.Service;

@Service
public class CloudGetFileUrlStrategy implements GetFileUrlStrategy {

    @Override
    public String execute(File file) {
        // TODO - Implement cloud storage
        throw new UnsupportedOperationException("Cloud storage not implemented yet");
    }
}