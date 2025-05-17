package com.devfiles.core.file.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.file.application.service.GetFileUrlService;
import com.devfiles.core.file.application.usecase.GetFileUseCase;
import com.devfiles.core.file.domain.File;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetFileUseCaseUnitTest {
    @InjectMocks private GetFileUseCase getFileUseCase;
    @Mock private UserService userService;
    @Mock private FileService fileService;
    @Mock private GetFileUrlService getFileUrlService;

    @Test
    void shouldGetFileWithSuccess() {
        var user = User.builder()
                .id(1L)
                .slug(Slug.of("test-user"))
                .build();

        var file = File.builder()
                .id(1L)
                .slug(Slug.of("test-file"))
                .name("test-file.txt")
                .mimeType("text/plain")
                .size(1024L)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        var fileUrl = "http://example.com/test-file.txt";

        when(userService.findBySlug(anyString())).thenReturn(user);
        when(fileService.findBySlugAndUserId(anyString(), anyLong())).thenReturn(file);
        when(getFileUrlService.execute(anyString(), anyLong())).thenReturn(fileUrl);

        var response = getFileUseCase.execute(user.getSlug().getValue(), file.getSlug().getValue());

        assertEquals(file.getSlug().getValue(), response.getData().getSlug());
        assertEquals(fileUrl, response.getData().getUrl());
        assertEquals("File found successfully", response.getMetadata().getMessage());

        verify(userService).findBySlug(user.getSlug().getValue());
        verify(fileService).findBySlugAndUserId(file.getSlug().getValue(), user.getId());
        verify(getFileUrlService).execute(file.getSlug().getValue(), user.getId());
    }
}
