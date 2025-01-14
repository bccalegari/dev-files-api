package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserAlreadyExistsException;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserResponseDto;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import com.devfiles.enterprise.abstraction.UseCase;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("createUserUseCase")
public class CreateUserUseCase implements UseCase<CreateUserRequestDto, CreateUserResponseDto> {
    private final UserMapper userMapper;
    private final UserRepositoryGateway userRepositoryGateway;

    public CreateUserUseCase(
        UserMapper userMapper,
        @Qualifier("userJpaRepositoryGateway") UserRepositoryGateway userRepositoryGateway
    ) {
        this.userMapper = userMapper;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Transactional
    public ResponseDto<CreateUserResponseDto> execute(CreateUserRequestDto createUserRequestDto) {
        var user = userMapper.toDomain(createUserRequestDto);

        if (userRepositoryGateway.exists(user)) {
            throw new UserAlreadyExistsException();
        }

        user = userRepositoryGateway.save(user);

        var createUserResponseDto = CreateUserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        return ResponseDto.success(createUserResponseDto, "User created successfully");
    }
}
