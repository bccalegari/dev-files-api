package com.devfiles.core.user.infrastructure.adapter.gateway;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Qualifier("userJpaRepositoryGateway")
public class UserJpaRepositoryGateway implements UserRepositoryGateway {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findBySlug(String slug) {
        return userRepository.findBySlugAndDeletedAtIsNull(slug).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmailAndDeletedAtIsNull(username, email).map(userMapper::toDomain);
    }

    @Override
    public boolean exists(User user) {
        var entity = userMapper.toEntity(user);

        if (entity.getId() != null) {
            return userRepository.existsByIdAndDeletedAtIsNull(entity.getId());
        }

        return userRepository.existsByUsernameOrEmailAndDeletedAtIsNull(entity.getUsername(), entity.getEmail());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndDeletedAtIsNull(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        entity = userRepository.saveAndFlush(entity);
        return userMapper.toDomain(entity);
    }
}
