package com.devfiles.core.file.application.service;

import com.devfiles.core.file.domain.NewFileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEmbeddingListener {
    private final RedissonClient redissonClient;
    private final FileEmbeddingService fileEmbeddingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void execute(NewFileEvent event) {
        var file = event.file();
        var lock = redissonClient.getLock("file-embedding-lock-" + file.getId());

        try {
            var isLocked = lock.tryLock(5, 60, TimeUnit.SECONDS);

            if (!isLocked) {
                log.warn("File lock is already taken for file: {}", file.getId());
                return;
            }

            fileEmbeddingService.execute(file);
        } catch (Exception e) {
            log.error("Error embedding file: {}", file.getId(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
