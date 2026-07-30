package com.walletko.backend.controller;

import com.walletko.backend.domain.entity.User;
import com.walletko.backend.domain.repository.UserRepository;
import com.walletko.backend.domain.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody SendCodeRequest request) {
        authService.sendOtpCode(request.email());
        return ResponseEntity.ok().body("Code de vérification envoyé");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthService.AuthResponse> verifyCode(@RequestBody VerifyCodeRequest request) {
        AuthService.AuthResponse response = authService.verifyOtpCode(request.email(), request.code());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) authentication.getPrincipal();
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record SendCodeRequest(String email) {}
    public record VerifyCodeRequest(String email, String code) {}
}
