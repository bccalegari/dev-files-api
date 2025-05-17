package com.devfiles.core.file.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.application.usecase.DeleteFileUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteFileUseCaseUnitTest {
    @InjectMocks private DeleteFileUseCase deleteFileUseCase;
    @Mock private UserService userService;
    @Mock private FileService fileService;
    @Mock private RedissonClient redissonClient;

    @Test
    @SneakyThrows
    void shouldReturnErrorWhenFileIsLocked() {
        var lockMock = mock(RLock.class);

        when(userService.findBySlug(anyString())).thenReturn(mock(User.class));
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(mock(File.class));
        when(redissonClient.getLock(anyString())).thenReturn(lockMock);
        when(lockMock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        var response = deleteFileUseCase.execute("test-user", "test-file");

        assertEquals("File is currently being processed by another request", response.getError().getMessage());
        assertEquals(ErrorCode.CONFLICT, response.getError().getCode());

        verifyNoMoreInteractions(fileService);
    }

    @Test
    @SneakyThrows
    void shouldReturnSuccessWhenFileIsDeleted() {
        var lockMock = mock(RLock.class);
        var userMock = mock(User.class);
        var fileMock = mock(File.class, CALLS_REAL_METHODS);

        when(userService.findBySlug(anyString())).thenReturn(userMock);
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(fileMock);
        when(redissonClient.getLock(anyString())).thenReturn(lockMock);
        when(lockMock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        var response = deleteFileUseCase.execute("test-user", "test-file");

        assertNotNull(fileMock.getDeletedAt());
        assertEquals("File deleted successfully", response.getMetadata().getMessage());
        assertNull(response.getError());

        verify(fileService).save(fileMock);
    }
}
