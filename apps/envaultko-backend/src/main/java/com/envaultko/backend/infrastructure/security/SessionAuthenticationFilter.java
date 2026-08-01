package com.envaultko.backend.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private final SessionManager sessionManager;

    public SessionAuthenticationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        var userOpt = sessionManager.getAuthenticatedUser(request);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var auth = new UsernamePasswordAuthenticationToken(
                user.getId(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
