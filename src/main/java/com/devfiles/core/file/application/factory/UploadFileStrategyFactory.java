package com.devfiles.core.file.application.factory;

import com.devfiles.core.file.abstraction.UploadFileStrategy;
import com.devfiles.core.file.application.strategy.CloudUploadFileStrategy;
import com.devfiles.core.file.application.strategy.LocalUploadFileStrategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadFileStrategyFactory {
    private final Map<String, UploadFileStrategy> strategyMap;
    private final Environment environment;

    public UploadFileStrategyFactory(
            Environment environment,
            LocalUploadFileStrategy localUploadFileStrategy,
            CloudUploadFileStrategy cloudUploadFileStrategy
    ) {
        this.strategyMap = Map.of(
                "local", localUploadFileStrategy,
                "cloud", cloudUploadFileStrategy
        );
        this.environment = environment;
    }

    public UploadFileStrategy getStrategy() {
        if (isLocalEnvironment()) {
            return strategyMap.get("local");
        }
        return strategyMap.get("cloud");
    }

    private boolean isLocalEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("local".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
