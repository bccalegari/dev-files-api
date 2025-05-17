package com.devfiles.core.file.usecase;

import com.devfiles.core.file.application.service.FileEmbeddingService;
import com.devfiles.core.file.application.service.FileQueryService;
import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.application.usecase.QueryFileUseCase;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.constant.ErrorCode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryFileUseCaseUnitTest {
    @InjectMocks private QueryFileUseCase queryFileUseCase;
    @Mock private UserService userService;
    @Mock private FileService fileService;
    @Mock private FileEmbeddingService fileEmbeddingService;
    @Mock private FileQueryService fileQueryService;
    @Mock private RedissonClient redissonClient;

    @Test
    @SneakyThrows
    void shouldReturnErrorWhenFileIsLocked() {
        var lockMock = mock(RLock.class);

        when(userService.findBySlug(anyString())).thenReturn(mock(User.class));
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(mock(File.class));
        when(redissonClient.getLock(anyString())).thenReturn(lockMock);
        when(lockMock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        var response = queryFileUseCase.execute("test-query", "test-user", "test-file");

        assertEquals("File is currently being processed by another request", response.getError().getMessage());
        assertEquals(ErrorCode.CONFLICT, response.getError().getCode());

        verifyNoInteractions(fileEmbeddingService);
        verifyNoInteractions(fileQueryService);
    }

    @Test
    @SneakyThrows
    void shouldEmbedFileWhenFileIsNotEmbedded() {
        var lockMock = mock(RLock.class);
        var userMock = mock(User.class);
        var fileMock = mock(File.class);

        when(userService.findBySlug(anyString())).thenReturn(userMock);
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(fileMock);
        when(redissonClient.getLock(anyString())).thenReturn(lockMock);
        when(lockMock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(fileMock.isEmbedded()).thenReturn(false);
        doNothing().when(fileEmbeddingService).execute(fileMock);
        when(fileQueryService.execute(anyString(), any(File.class))).thenReturn("test-query");

        var response = queryFileUseCase.execute("test-query", "test-user", "test-file");

        assertEquals("Answer retrieved successfully", response.getMetadata().getMessage());
        assertEquals("test-query", response.getData().getAnswer());

        verify(fileEmbeddingService).execute(fileMock);
        verify(fileQueryService).execute("test-query", fileMock);
    }

    @Test
    @SneakyThrows
    void shouldNotEmbedFileWhenFileIsAlreadyEmbedded() {
        var lockMock = mock(RLock.class);
        var userMock = mock(User.class);
        var fileMock = mock(File.class);

        when(userService.findBySlug(anyString())).thenReturn(userMock);
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(fileMock);
        when(redissonClient.getLock(anyString())).thenReturn(lockMock);
        when(lockMock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(fileMock.isEmbedded()).thenReturn(true);
        when(fileQueryService.execute(anyString(), any(File.class))).thenReturn("test-query");

        var response = queryFileUseCase.execute("test-query", "test-user", "test-file");

        assertEquals("Answer retrieved successfully", response.getMetadata().getMessage());
        assertEquals("test-query", response.getData().getAnswer());

        verifyNoInteractions(fileEmbeddingService);
        verify(fileQueryService).execute("test-query", fileMock);
    }
}
