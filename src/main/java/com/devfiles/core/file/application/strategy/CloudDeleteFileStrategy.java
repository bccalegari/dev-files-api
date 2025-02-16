package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.DeleteFileStrategy;
import com.devfiles.core.file.domain.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudDeleteFileStrategy implements DeleteFileStrategy {
    @Override
    public void execute(File file) {
        // TODO - Implement cloud storage
        throw new UnsupportedOperationException("Cloud storage not implemented yet");
    }
}