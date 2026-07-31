package com.walletko.backend.infrastructure.security;

import com.walletko.backend.domain.auth.AuthenticatedUser;
import com.walletko.backend.domain.auth.SessionPort;
import com.walletko.backend.domain.shared.vo.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class SessionAdapter implements SessionPort {

    private final SessionManager sessionManager;

    public SessionAdapter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public AuthenticatedUser createSession(String email, HttpServletResponse response) {
        var entity = sessionManager.createSession(email, response);
        return new AuthenticatedUser(
            new Id(entity.getId()),
            entity.getName(),
            entity.getEmail(),
            entity.isEmailVerified()
        );
    }

    @Override
    public void destroySession(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.destroySession(request, response);
    }
}
