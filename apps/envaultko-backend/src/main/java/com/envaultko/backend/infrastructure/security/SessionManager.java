package com.envaultko.backend.infrastructure.security;

import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.infrastructure.persistence.entity.SessionEntity;
import com.envaultko.backend.infrastructure.persistence.entity.UserEntity;
import com.envaultko.backend.infrastructure.persistence.repository.SessionJpaRepository;
import com.envaultko.backend.infrastructure.persistence.repository.UserJpaRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class SessionManager {
    private static final String COOKIE_NAME = "envaultko_session";

    private final SessionJpaRepository sessionRepo;
    private final UserJpaRepository userRepo;
    private final long sessionDurationHours;

    public SessionManager(SessionJpaRepository sessionRepo,
                           UserJpaRepository userRepo,
                           @Value("${envaultko.auth.otp.expiry-seconds:300}") long expirySeconds) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.sessionDurationHours = 168; // 7 days
    }

    @Transactional
    public UserEntity createSession(String email, HttpServletResponse response) {
        var user = userRepo.findByEmail(email).orElseGet(() -> {
            var newUser = new UserEntity();
            newUser.setId(java.util.UUID.randomUUID().toString());
            newUser.setName(email.split("@")[0]);
            newUser.setEmail(email);
            newUser.setEmailVerified(true);
            newUser.setCreatedAt(OffsetDateTime.now());
            newUser.setUpdatedAt(OffsetDateTime.now());
            return userRepo.save(newUser);
        });

        var session = new SessionEntity();
        session.setId(java.util.UUID.randomUUID().toString());
        session.setToken(java.util.UUID.randomUUID().toString());
        session.setUserId(user.getId());
        session.setExpiresAt(OffsetDateTime.now().plusHours(sessionDurationHours));
        session.setCreatedAt(OffsetDateTime.now());
        session.setUpdatedAt(OffsetDateTime.now());
        sessionRepo.save(session);

        var cookie = new Cookie(COOKIE_NAME, session.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");
        cookie.setMaxAge((int) (sessionDurationHours * 3600));
        response.addCookie(cookie);

        return user;
    }

    public Optional<UserEntity> getAuthenticatedUser(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies == null) return Optional.empty();

        String token = null;
        for (var c : cookies) {
            if (COOKIE_NAME.equals(c.getName())) {
                token = c.getValue();
                break;
            }
        }
        if (token == null || token.isBlank()) return Optional.empty();

        return sessionRepo.findByToken(token)
            .filter(s -> s.getExpiresAt().isAfter(OffsetDateTime.now()))
            .flatMap(s -> userRepo.findById(s.getUserId()));
    }

    @Transactional
    public void destroySession(HttpServletRequest request, HttpServletResponse response) {
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    sessionRepo.deleteByToken(c.getValue());
                    break;
                }
            }
        }
        var cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
