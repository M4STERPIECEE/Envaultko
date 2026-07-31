package com.walletko.backend.interfaces.rest;

import com.walletko.backend.infrastructure.security.OtpService;
import com.walletko.backend.infrastructure.security.SessionManager;
import com.walletko.backend.infrastructure.persistence.repository.UserJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final OtpService otpService;
    private final SessionManager sessionManager;
    private final UserJpaRepository userRepo;

    public AuthController(OtpService otpService, SessionManager sessionManager,
                           UserJpaRepository userRepo) {
        this.otpService = otpService;
        this.sessionManager = sessionManager;
        this.userRepo = userRepo;
    }

    @PostMapping("/email-otp/send-verification-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        var email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        otpService.sendOtp(email);
        return ResponseEntity.ok(Map.of("message", "Code sent"));
    }

    @PostMapping("/sign-in/email-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body,
                                        HttpServletResponse response) {
        var email = body.get("email");
        var otp = body.get("otp");
        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and OTP are required"));
        }
        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired code"));
        }
        var user = sessionManager.createSession(email, response);
        return ResponseEntity.ok(Map.of(
            "user", Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail()),
            "session", Map.of("id", user.getId())
        ));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.destroySession(request, response);
        return ResponseEntity.ok(Map.of("message", "Signed out"));
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.ok(Map.of("user", null, "session", null));
        }
        var userId = (String) authentication.getPrincipal();
        var userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(Map.of("user", null, "session", null));
        }
        var user = userOpt.get();
        return ResponseEntity.ok(Map.of(
            "user", Map.of("id", user.getId(), "name", user.getName(),
                           "email", user.getEmail(), "emailVerified", user.isEmailVerified()),
            "session", Map.of("id", user.getId())
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        var userId = (String) authentication.getPrincipal();
        return userRepo.findById(userId)
            .map(u -> ResponseEntity.ok(Map.of(
                "id", u.getId(), "name", u.getName(), "email", u.getEmail())))
            .orElse(ResponseEntity.notFound().build());
    }
}
