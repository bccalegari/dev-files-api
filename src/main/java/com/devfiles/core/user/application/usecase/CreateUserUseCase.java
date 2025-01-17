package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserAlreadyExistsException;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserResponseDto;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final UserRepositoryGateway userRepositoryGateway;

    public CreateUserUseCase(
        UserMapper userMapper,
        @Qualifier("userJpaRepositoryGateway") UserRepositoryGateway userRepositoryGateway
    ) {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.userMapper = userMapper;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Transactional
    public ResponseDto<CreateUserResponseDto> execute(CreateUserRequestDto createUserRequestDto) {
        createUserRequestDto.setPassword(bCryptPasswordEncoder.encode(createUserRequestDto.getPassword()));

        var user = userMapper.toDomain(createUserRequestDto);

        if (userRepositoryGateway.exists(user)) {
            throw new UserAlreadyExistsException();
        }

        user = userRepositoryGateway.save(user);

        var createUserResponseDto = CreateUserResponseDto.builder()
                .slug(user.getSlug().getValue())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        return ResponseDto.success(createUserResponseDto, "User created successfully");
    }
}
