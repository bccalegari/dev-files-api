package com.devfiles.core.user.application.usecase;

import com.devfiles.core.file.application.service.FileService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {
    private final UserService userService;
    private final FileService fileService;

    @Transactional
    public ResponseDto<ResponseDto.Empty> execute(String userSlug) {
        var user = userService.findBySlug(userSlug);

        user.delete();

        user = userService.save(user);

        fileService.markAllFilesForRemovalByUserId(user.getId());

        return ResponseDto.success("User deleted successfully");
    }
}
