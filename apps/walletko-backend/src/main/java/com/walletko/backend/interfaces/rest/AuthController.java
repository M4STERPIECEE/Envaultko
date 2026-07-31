package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.auth.AuthService;
import com.walletko.backend.domain.auth.AuthenticatedUser;
import com.walletko.backend.interfaces.dto.AuthSessionDTO;
import com.walletko.backend.interfaces.dto.AuthUserDTO;
import com.walletko.backend.interfaces.dto.MessageResponse;
import com.walletko.backend.interfaces.dto.SendOtpRequest;
import com.walletko.backend.interfaces.dto.SessionRefDTO;
import com.walletko.backend.interfaces.dto.VerifyOtpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/email-otp/send-verification-otp")
    public ResponseEntity<MessageResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        authService.sendOtp(request.email());
        return ResponseEntity.ok(new MessageResponse("Code sent"));
    }

    @PostMapping("/sign-in/email-otp")
    public ResponseEntity<AuthSessionDTO> verifyOtp(@Valid @RequestBody VerifyOtpRequest request,
                                                    HttpServletResponse response) {
        var user = authService.signInWithOtp(request.email(), request.otp(), response);
        return ResponseEntity.ok(toAuthSession(user));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<MessageResponse> signOut(HttpServletRequest request,
                                                   HttpServletResponse response) {
        authService.signOut(request, response);
        return ResponseEntity.ok(new MessageResponse("Signed out"));
    }

    @GetMapping("/session")
    public ResponseEntity<AuthSessionDTO> getSession(Authentication authentication) {
        return authService.currentUser(authentication)
            .map(user -> ResponseEntity.ok(toAuthSession(user)))
            .orElse(ResponseEntity.ok(new AuthSessionDTO(null, null)));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDTO> me(Authentication authentication) {
        return authService.currentUser(authentication)
            .map(user -> ResponseEntity.ok(toUser(user)))
            .orElse(ResponseEntity.status(401).build());
    }

    private AuthSessionDTO toAuthSession(AuthenticatedUser user) {
        return new AuthSessionDTO(toUser(user), new SessionRefDTO(user.id().value()));
    }

    private AuthUserDTO toUser(AuthenticatedUser user) {
        return new AuthUserDTO(user.id().value(), user.name(), user.email(), user.emailVerified());
    }
}

