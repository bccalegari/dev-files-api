package com.devfiles.enterprise.application.service;

import com.devfiles.core.user.application.exception.UserNotAllowedToManageAnotherUserResourcesException;
import org.springframework.stereotype.Service;

@Service
public class UserAllowedToManageAnotherUserResourcesValidator {
    public void execute(String loggedInUserSlug, String userSlug) {
        if (!loggedInUserSlug.equals(userSlug)) {
            throw new UserNotAllowedToManageAnotherUserResourcesException();
        }
    }
}