package com.devfiles.enterprise.infrastructure.adapter.interceptor;

import com.devfiles.core.user.application.exception.UserNotAllowedToManageAnotherUserResourcesException;
import com.devfiles.enterprise.infrastructure.adapter.UserAllowedToManageAnotherUserResourcesValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

public class UserAuthorizationAspectIntegrationTest {
    private static UserAuthorizationAspect userAuthorizationAspect;
    private static final UserAllowedToManageAnotherUserResourcesValidator
            userAllowedToManageAnotherUserResourcesValidator = new UserAllowedToManageAnotherUserResourcesValidator();

    @BeforeAll
    public static void setUp() {
        userAuthorizationAspect = new UserAuthorizationAspect(userAllowedToManageAnotherUserResourcesValidator);
    }

    @Test
    void shouldValidateUserAuthorizationWhenUserIsAllowedToManageAnotherUserResources() {
        var joinPoint = mock(ProceedingJoinPoint.class);
        var args = new String[] {"user-slug", "user-slug"};
        var signature = mock(MethodSignature.class);
        Parameter[] parameters = new Parameter [] {
            mock(Parameter.class),
            mock(Parameter.class)
        };

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(mock(Method.class));
        when(signature.getMethod().getParameters()).thenReturn(parameters);
        when(parameters[0].getName()).thenReturn("loggedInUserSlug");
        when(parameters[1].getName()).thenReturn("userSlug");

        try {
            userAuthorizationAspect.validateUserAuthorization(joinPoint);
        } catch (Throwable e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenLoggedInUserSlugIsMissing() {
        var joinPoint = mock(ProceedingJoinPoint.class);
        var args = new String[]{null, "user-slug"};
        var signature = mock(MethodSignature.class);
        Parameter[] parameters = new Parameter[]{
                mock(Parameter.class),
                mock(Parameter.class)
        };

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(mock(Method.class));
        when(signature.getMethod().getParameters()).thenReturn(parameters);
        when(parameters[0].getName()).thenReturn("loggedInUserSlug");
        when(parameters[1].getName()).thenReturn("userSlug");

        assertThrows(IllegalArgumentException.class,
                () -> userAuthorizationAspect.validateUserAuthorization(joinPoint));
    }

    @Test
    void shouldThrowUserNotAllowedToManageAnotherUserResourcesExceptionWhenUserIsNotAllowedToManageAnotherUserResources() {
        var joinPoint = mock(ProceedingJoinPoint.class);
        var args = new String[] {"user-slug", "another-user-slug"};
        var signature = mock(MethodSignature.class);
        Parameter[] parameters = new Parameter [] {
            mock(Parameter.class),
            mock(Parameter.class)
        };

        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(mock(Method.class));
        when(signature.getMethod().getParameters()).thenReturn(parameters);
        when(parameters[0].getName()).thenReturn("loggedInUserSlug");
        when(parameters[1].getName()).thenReturn("userSlug");

        assertThrows(UserNotAllowedToManageAnotherUserResourcesException.class,
                () -> userAuthorizationAspect.validateUserAuthorization(joinPoint));
    }
}
