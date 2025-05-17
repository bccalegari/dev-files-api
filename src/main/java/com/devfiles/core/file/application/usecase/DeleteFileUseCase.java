package com.devfiles.core.file.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFileUseCase {
    private final UserService userService;
    private final FileService fileService;
    private final RedissonClient redissonClient;

    @SneakyThrows
    public ResponseDto<ResponseDto.Empty> execute(String userSlug, String fileSlug) {
        var user = userService.findBySlug(userSlug);

        var file = fileService.findBySlugAndUserId(fileSlug, user.getId());

        var fileLock = redissonClient.getLock("file-lock:" + file.getId());

        if (!fileLock.tryLock(0, 10, TimeUnit.SECONDS)) {
            return ResponseDto.error(
                    ErrorCode.CONFLICT, "File is currently being processed by another request"
            );
        }

        file.delete();
        fileService.save(file);

        return ResponseDto.success("File deleted successfully");
    }
}
