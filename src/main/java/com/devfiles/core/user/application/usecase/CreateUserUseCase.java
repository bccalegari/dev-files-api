package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyExistsException;
import com.devfiles.core.user.application.service.UserMessageBrokerService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserResponseDto;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserMapper userMapper;
    private final UserService userService;
    private final InvitationService invitationService;
    private final UserMessageBrokerService userMessageBrokerService;

    @Transactional
    public ResponseDto<CreateUserResponseDto> execute(CreateUserRequestDto createUserRequestDto) {
        createUserRequestDto.setPassword(bCryptPasswordEncoder.encode(createUserRequestDto.getPassword()));

        var user = userMapper.toDomain(createUserRequestDto);

        if (userService.exists(user)) {
            throw new UserAlreadyExistsException();
        }

        user = userService.save(user);

        var createUserResponseDto = CreateUserResponseDto.builder()
                .slug(user.getSlug().getValue())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        var invitation = createInvitation(user);

        userMessageBrokerService.sendInvitationRegistrationMessage(invitation);

        return ResponseDto.success(createUserResponseDto, "User created successfully");
    }

    private Invitation createInvitation(User user) {
        var invitation = Invitation.builder()
                .user(user)
                .code(new InvitationCode())
                .build();
        return invitationService.save(invitation);
    }
}
