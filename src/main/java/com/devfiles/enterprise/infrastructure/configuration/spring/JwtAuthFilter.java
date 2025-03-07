package com.devfiles.enterprise.infrastructure.configuration.spring;

import com.devfiles.enterprise.infrastructure.configuration.jwt.JwtTokenDecoder;
import com.devfiles.enterprise.infrastructure.configuration.jwt.JwtTokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenDecoder jwtTokenDecoder;
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var bearerToken = getBearerToken(request);

        if (bearerToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var decodedJwt = jwtTokenDecoder.execute(bearerToken);

        if (!jwtTokenValidator.execute(decodedJwt)) {
            filterChain.doFilter(request, response);
        }

        var subject = decodedJwt.getSubject();

        setRequestAttribute(request, subject);
        filterChain.doFilter(request, response);
    }

    private String getBearerToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : "";
    }

    private void setRequestAttribute(HttpServletRequest request, String subject) {
        if (!subject.isEmpty()) {
            request.setAttribute("logged_in_user_slug", subject);
            setSpringSecurityContext(subject);
        }
    }

    private void setSpringSecurityContext(String subject) {
        var authentication = new UsernamePasswordAuthenticationToken(subject, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}