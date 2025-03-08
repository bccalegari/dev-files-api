package com.devfiles.core.file.infrastructure.adapter.schedule;

import com.devfiles.core.file.application.factory.DeleteFileStrategyFactory;
import com.devfiles.core.file.application.service.FileService;
import com.devfiles.enterprise.domain.valueobject.TraceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ForkJoinPool;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteMarkedForRemovalFilesSchedule {
    private final static int PARALLELISM_LEVEL = 2;

    private final FileService fileService;
    private final DeleteFileStrategyFactory deleteFileStrategyFactory;

    @Scheduled(cron = "0 0 */6 * * *")
    public void execute() {
        var traceId = new TraceId(MDC.get(TraceId.TRACE_ID_MDC_KEY));
        traceId.registerMdcLog();
        try {
            log.info("Deleting files marked for removal with {} threads", PARALLELISM_LEVEL);
            var startTime = System.currentTimeMillis();

            var files = fileService.findAllFilesMarkedForRemoval();

            if (files.isEmpty()) {
                log.info("No files marked for removal");
                return;
            }

            var deleteFileStrategy = deleteFileStrategyFactory.getStrategy();

            try (var forkJoinPool = new ForkJoinPool(PARALLELISM_LEVEL)) {
                forkJoinPool.submit(() -> files.parallelStream()
                        .forEach(file -> {
                            var contextMap = MDC.getCopyOfContextMap();
                            if (contextMap != null) {
                                MDC.setContextMap(contextMap);
                            }
                            try {
                                deleteFileStrategy.execute(file);
                            } finally {
                                MDC.clear();
                            }
                        })).get();

                fileService.hardDeleteAll(files);
            } catch (Exception e) {
                log.error("Error while deleting files", e);
            }

            var endTime = System.currentTimeMillis();
            log.info("Deleted {} files marked for removal in {} ms", files.size(), endTime - startTime);
        } finally {
            MDC.clear();
        }
    }
}
