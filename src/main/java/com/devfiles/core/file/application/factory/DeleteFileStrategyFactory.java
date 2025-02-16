package com.devfiles.core.file.application.factory;

import com.devfiles.core.file.abstraction.DeleteFileStrategy;
import com.devfiles.core.file.application.strategy.CloudDeleteFileStrategy;
import com.devfiles.core.file.application.strategy.LocalDeleteFileStrategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeleteFileStrategyFactory {
    private final Map<String, DeleteFileStrategy> strategyMap;
    private final Environment environment;

    public DeleteFileStrategyFactory(
            Environment environment,
            LocalDeleteFileStrategy localDeleteFileStrategy,
            CloudDeleteFileStrategy cloudDeleteFileStrategy
    ) {
        this.strategyMap = Map.of(
                "local", localDeleteFileStrategy,
                "cloud", cloudDeleteFileStrategy
        );
        this.environment = environment;
    }

    public DeleteFileStrategy getStrategy() {
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
