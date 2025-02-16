package com.devfiles.core.user.domain;

import com.devfiles.enterprise.domain.entity.BaseDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class User extends BaseDomain {
    @Setter private String username;
    @Setter private String password;
    @Setter private String email;
    private boolean active;

    public void activate() {
        active = true;
    }
}
