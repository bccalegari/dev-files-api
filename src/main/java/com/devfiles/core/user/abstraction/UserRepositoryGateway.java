package com.devfiles.core.user.abstraction;

import com.devfiles.core.user.domain.User;

public interface UserRepositoryGateway {
    boolean exists(User user);
    User save(User user);
}