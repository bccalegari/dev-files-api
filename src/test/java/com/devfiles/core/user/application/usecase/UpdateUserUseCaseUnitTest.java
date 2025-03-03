package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyExistsException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.dto.UpdateUserRequestDto;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseUnitTest {
    @Mock private UserService userService;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UpdateUserUseCase updateUserUseCase;

    @Test
    void shouldUpdateUser() {
        var userSlug = "user-slug";
        var updateUserRequestDto = UpdateUserRequestDto.builder()
                .username("new-username")
                .email("new-email")
                .password("new-password")
                .passwordConfirmation("new-password")
                .build();

        var user = User.builder()
                .slug(Slug.of(userSlug))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userService.findBySlug(userSlug)).thenReturn(user);
        when(userService.existsByUsername(updateUserRequestDto.getUsername())).thenReturn(false);
        when(userService.existsByEmail(updateUserRequestDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(updateUserRequestDto.getPassword())).thenReturn("encoded-password");
        when(userService.save(user)).thenReturn(user);

        var response = updateUserUseCase.execute(userSlug, updateUserRequestDto);

        assertNotNull(user.getUpdatedAt());
        assertEquals(updateUserRequestDto.getUsername(), user.getUsername());
        assertEquals(updateUserRequestDto.getEmail(), user.getEmail());
        assertEquals("encoded-password", user.getPassword());

        assertNotNull(response);
        assertEquals(userSlug, response.getData().getSlug());
        assertEquals(updateUserRequestDto.getUsername(), response.getData().getUsername());
        assertEquals(updateUserRequestDto.getEmail(), response.getData().getEmail());

        verify(userService).findBySlug(userSlug);
        verify(userService).existsByUsername(updateUserRequestDto.getUsername());
        verify(userService).existsByEmail(updateUserRequestDto.getEmail());
        verify(passwordEncoder).encode(updateUserRequestDto.getPassword());
        verify(userService).save(user);
    }

    @Test
    void shouldUpdateUserPartially() {
        var userSlug = "user-slug";
        var updateUserRequestDto = UpdateUserRequestDto.builder()
                .username("new-username")
                .build();

        var user = User.builder()
                .slug(Slug.of(userSlug))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userService.findBySlug(userSlug)).thenReturn(user);
        when(userService.existsByUsername(updateUserRequestDto.getUsername())).thenReturn(false);
        when(userService.save(user)).thenReturn(user);

        var response = updateUserUseCase.execute(userSlug, updateUserRequestDto);

        assertNotNull(user.getUpdatedAt());
        assertEquals(updateUserRequestDto.getUsername(), user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());

        assertNotNull(response);
        assertEquals(userSlug, response.getData().getSlug());
        assertEquals(updateUserRequestDto.getUsername(), response.getData().getUsername());
        assertEquals("email", response.getData().getEmail());

        verify(userService).findBySlug(userSlug);
        verify(userService).existsByUsername(updateUserRequestDto.getUsername());
        verify(userService).save(user);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUsernameAlreadyExists() {
        var userSlug = "user-slug";
        var updateUserRequestDto = UpdateUserRequestDto.builder()
                .username("new-username")
                .build();

        var user = User.builder()
                .slug(Slug.of(userSlug))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userService.findBySlug(userSlug)).thenReturn(user);
        when(userService.existsByUsername(updateUserRequestDto.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> updateUserUseCase.execute(userSlug, updateUserRequestDto));

        verify(userService).findBySlug(userSlug);
        verify(userService).existsByUsername(updateUserRequestDto.getUsername());
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyExists() {
        var userSlug = "user-slug";
        var updateUserRequestDto = UpdateUserRequestDto.builder()
                .email("new-email")
                .build();

        var user = User.builder()
                .slug(Slug.of(userSlug))
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(userService.findBySlug(userSlug)).thenReturn(user);
        when(userService.existsByEmail(updateUserRequestDto.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> updateUserUseCase.execute(userSlug, updateUserRequestDto));

        verify(userService).findBySlug(userSlug);
        verify(userService).existsByEmail(updateUserRequestDto.getEmail());
    }
}
