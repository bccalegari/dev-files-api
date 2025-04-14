package com.devfiles.core.file.application.service;

import com.devfiles.core.file.domain.NewFileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEmbeddingListener {
    private final FileEmbeddingService fileEmbeddingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void execute(NewFileEvent event) {
        var file = event.file();
        try {
            fileEmbeddingService.execute(file);
        } catch (Exception e) {
            log.error("Error embedding file: {}", file.getId(), e);
        }

    }
}
