package com.devfiles.core.user.domain;

import com.devfiles.enterprise.domain.entity.BaseDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class User extends BaseDomain {
    private String username;
    private String password;
    private String email;
    private boolean active;

    public void activate() {
        active = true;
    }
}
