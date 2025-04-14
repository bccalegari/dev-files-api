package com.devfiles.enterprise.infrastructure.configuration.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        return new VirtualThreadMdcExecutor(new MdcTaskDecorator());
    }
}
