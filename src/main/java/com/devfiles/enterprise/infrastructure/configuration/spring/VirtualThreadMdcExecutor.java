package com.devfiles.enterprise.infrastructure.configuration.spring;

import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.Executor;

public class VirtualThreadMdcExecutor implements Executor {

    private final TaskDecorator taskDecorator;

    public VirtualThreadMdcExecutor(TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }

    @Override
    public void execute(Runnable command) {
        Runnable decorated = taskDecorator.decorate(command);
        Thread.startVirtualThread(decorated);
    }
}
