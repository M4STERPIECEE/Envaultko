package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.auth.AuthService;
import com.walletko.backend.interfaces.dto.AuthSessionDTO;
import com.walletko.backend.interfaces.dto.AuthUserDTO;
import com.walletko.backend.interfaces.dto.request.SendOtpRequest;
import com.walletko.backend.interfaces.dto.request.VerifyOtpRequest;
import com.walletko.backend.interfaces.dto.response.MessageResponse;
import com.walletko.backend.interfaces.mapper.AuthViewMapper;
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
    private final AuthViewMapper authViewMapper;

    public AuthController(AuthService authService, AuthViewMapper authViewMapper) {
        this.authService = authService;
        this.authViewMapper = authViewMapper;
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
        return ResponseEntity.ok(authViewMapper.toSession(user));
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
            .map(user -> ResponseEntity.ok(authViewMapper.toSession(user)))
            .orElse(ResponseEntity.ok(new AuthSessionDTO(null, null)));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDTO> me(Authentication authentication) {
        return authService.currentUser(authentication)
            .map(user -> ResponseEntity.ok(authViewMapper.toUser(user)))
            .orElse(ResponseEntity.status(401).build());
    }
}

