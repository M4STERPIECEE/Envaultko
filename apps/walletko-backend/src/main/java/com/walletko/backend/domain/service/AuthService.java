package com.walletko.backend.domain.service;

import com.walletko.backend.domain.entity.User;
import com.walletko.backend.domain.repository.UserRepository;
import com.walletko.backend.security.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // Code OTP temporaire (Simulé ou envoyé par Email)
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public void sendOtpCode(String email) {
        String code = "123456"; // Code de démonstration / dev
        otpStore.put(email, code);
        // Possibilité d'intégrer JavaMailSender ici
    }

    public AuthResponse verifyOtpCode(String email, String code) {
        String storedCode = otpStore.get(email);
        if (storedCode == null || !storedCode.equals(code)) {
            // Permettre la connexion de test avec "123456"
            if (!"123456".equals(code)) {
                throw new IllegalArgumentException("Code de vérification invalide ou expiré");
            }
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setId(UUID.randomUUID().toString().replace("-", ""));
                    newUser.setEmail(email);
                    newUser.setName(email.split("@")[0]);
                    newUser.setEmailVerified(true);
                    return userRepository.save(newUser);
                });

        String token = jwtUtils.generateToken(user.getId(), user.getEmail());
        otpStore.remove(email);

        return new AuthResponse(token, user);
    }

    public record AuthResponse(String token, User user) {}
}
