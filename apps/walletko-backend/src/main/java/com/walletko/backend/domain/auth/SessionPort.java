package com.walletko.backend.domain.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SessionPort {
    AuthenticatedUser createSession(String email, HttpServletResponse response);
    void destroySession(HttpServletRequest request, HttpServletResponse response);
}
