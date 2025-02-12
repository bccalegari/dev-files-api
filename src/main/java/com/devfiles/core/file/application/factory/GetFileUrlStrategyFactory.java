package com.devfiles.core.file.application.factory;

import com.devfiles.core.file.abstraction.GetFileUrlStrategy;
import com.devfiles.core.file.application.strategy.CloudGetFileUrlStrategy;
import com.devfiles.core.file.application.strategy.LocalGetFileUrlStrategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetFileUrlStrategyFactory {
    private final Map<String, GetFileUrlStrategy> getFileUrlStrategyMap;
    private final Environment environment;

    public GetFileUrlStrategyFactory(
            Environment environment,
            LocalGetFileUrlStrategy localGetFileUrlStrategy,
            CloudGetFileUrlStrategy cloudGetFileUrlStrategy
    ) {
        this.getFileUrlStrategyMap = Map.of(
                "local", localGetFileUrlStrategy,
                "cloud", cloudGetFileUrlStrategy
        );
        this.environment = environment;
    }

    public GetFileUrlStrategy getFileUrlStrategy() {
        if (isLocalEnvironment()) {
            return getFileUrlStrategyMap.get("local");
        }
        return getFileUrlStrategyMap.get("cloud");
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
