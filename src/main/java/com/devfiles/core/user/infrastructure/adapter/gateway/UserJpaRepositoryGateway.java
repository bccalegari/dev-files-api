package com.devfiles.core.user.infrastructure.adapter.gateway;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("userJpaRepositoryGateway")
public class UserJpaRepositoryGateway implements UserRepositoryGateway {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean exists(User user) {
        var entity = userMapper.toEntity(user);

        if (entity.getId() != null) {
            return userRepository.existsById(entity.getId());
        }

        return userRepository.existsByUsernameOrEmailAndDeletedAtIsNull(entity.getUsername(), entity.getEmail());
    }

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        entity = userRepository.saveAndFlush(entity);
        return userMapper.toDomain(entity);
    }
}
