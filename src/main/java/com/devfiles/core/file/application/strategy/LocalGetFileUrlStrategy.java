package com.devfiles.core.file.application.strategy;

import com.devfiles.core.file.abstraction.GetFileUrlStrategy;
import com.devfiles.core.file.domain.File;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalGetFileUrlStrategy implements GetFileUrlStrategy {
    private final Environment environment;

    @Override
    public String execute(File file) {
        var port = environment.getProperty("server.port");
        var url = "localhost:%s/users/%s/files/%s/resources";
        return String.format(url, port, file.getUser().getSlug().getValue(), file.getSlug().getValue());
    }
}