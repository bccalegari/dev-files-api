package com.devfiles.core.user.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseUnitTest {
    @Mock private UserService userService;
    @Mock private FileService fileService;
    @InjectMocks private DeleteUserUseCase deleteUserUseCase;

    @Test
    void shouldDeleteUser() {
        var user = User.builder().id(1L).slug(Slug.of("user-slug")).build();

        when(userService.findBySlug(user.getSlug().getValue())).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        doNothing().when(fileService).markAllFilesForRemovalByUserId(user.getId());

        deleteUserUseCase.execute(user.getSlug().getValue());

        verify(userService).findBySlug(user.getSlug().getValue());
        verify(userService).save(user);
        verify(fileService).markAllFilesForRemovalByUserId(user.getId());
        verifyNoMoreInteractions(userService, fileService);

        assertNotNull(user.getDeletedAt());
    }
}
