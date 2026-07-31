package com.walletko.backend.application.auth;

import com.walletko.backend.domain.auth.AuthenticatedUser;
import com.walletko.backend.domain.auth.OtpPort;
import com.walletko.backend.domain.auth.SessionPort;
import com.walletko.backend.domain.auth.UserQueryPort;
import com.walletko.backend.domain.shared.vo.Id;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final OtpPort otpPort;
    private final SessionPort sessionPort;
    private final UserQueryPort userQueryPort;

    public AuthService(OtpPort otpPort, SessionPort sessionPort, UserQueryPort userQueryPort) {
        this.otpPort = otpPort;
        this.sessionPort = sessionPort;
        this.userQueryPort = userQueryPort;
    }

    public void sendOtp(String email) {
        otpPort.sendOtp(email);
    }

    @Transactional
    public AuthenticatedUser signInWithOtp(String email, String otp, HttpServletResponse response) {
        if (!otpPort.verifyOtp(email, otp)) {
            throw new InvalidOtpError("Invalid or expired code");
        }
        return sessionPort.createSession(email, response);
    }

    public Optional<AuthenticatedUser> currentUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return Optional.empty();
        }
        return userQueryPort.findById(new Id((String) auth.getPrincipal()));
    }

    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        sessionPort.destroySession(request, response);
    }
}

