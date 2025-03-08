package com.devfiles.enterprise.infrastructure.adapter.interceptor;

import com.devfiles.enterprise.infrastructure.adapter.validator.UserAllowedToManageAnotherUserResourcesValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthorizationAspect {
    private final UserAllowedToManageAnotherUserResourcesValidator validator;

    @Around("@annotation(com.devfiles.enterprise.infrastructure.annotation.UserAuthorizationValidator)")
    public Object validateUserAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();

        String loggedInUserSlug = null;
        String userSlug = null;

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals("loggedInUserSlug")) {
                loggedInUserSlug = (String) args[i];
            } else if (parameters[i].getName().equals("userSlug")) {
                userSlug = (String) args[i];
            }
        }

        if (loggedInUserSlug == null || userSlug == null) {
            throw new IllegalArgumentException(
                    "loggedInUserSlug and userSlug must be provided to validate user authorization"
            );
        }

        validator.execute(loggedInUserSlug, userSlug);

        return joinPoint.proceed();
    }
}
