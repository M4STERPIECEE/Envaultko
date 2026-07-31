package com.walletko.backend.infrastructure.security;

import com.walletko.backend.infrastructure.mail.SmtpMailer;
import com.walletko.backend.infrastructure.persistence.entity.VerificationEntity;
import com.walletko.backend.infrastructure.persistence.repository.VerificationJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);

    private final VerificationJpaRepository verificationRepo;
    private final SmtpMailer mailer;
    private final int otpLength;
    private final long expirySeconds;
    private final int maxAttempts;

    public OtpService(VerificationJpaRepository verificationRepo,
                      SmtpMailer mailer,
                      @Value("${walletko.auth.otp.length}") int otpLength,
                      @Value("${walletko.auth.otp.expiry-seconds}") long expirySeconds,
                      @Value("${walletko.auth.otp.max-attempts}") int maxAttempts) {
        this.verificationRepo = verificationRepo;
        this.mailer = mailer;
        this.otpLength = otpLength;
        this.expirySeconds = expirySeconds;
        this.maxAttempts = maxAttempts;
    }

    @Transactional
    public void sendOtp(String email) {
        var secureRandom = new SecureRandom();
        var sb = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        String otp = sb.toString();

        String hashed = Integer.toHexString(otp.hashCode());

        verificationRepo.deleteByIdentifier(email);

        var entity = new VerificationEntity();
        entity.setId(java.util.UUID.randomUUID().toString());
        entity.setIdentifier(email);
        entity.setValue(hashed);
        entity.setExpiresAt(OffsetDateTime.now().plusSeconds(expirySeconds));
        entity.setCreatedAt(OffsetDateTime.now());
        verificationRepo.save(entity);

        log.info("==================================================");
        log.info("🔐 [DEV MODE] OTP CODE FOR {}: {}", email, otp);
        log.info("==================================================");

        try {
            mailer.sendOtpEmail(email, otp);
            log.info("OTP email sent successfully to {}", email);
        } catch (Exception e) {
            log.warn("SMTP delivery skipped/failed ({}). Using console OTP above.", e.getMessage());
        }
    }

    @Transactional
    public boolean verifyOtp(String email, String code) {
        var optEntity = verificationRepo.findByIdentifier(email);
        if (optEntity.isEmpty()) {
            return false;
        }
        var entity = optEntity.get();

        if (entity.getExpiresAt().isBefore(OffsetDateTime.now())) {
            verificationRepo.delete(entity);
            return false;
        }

        String hashedInput = Integer.toHexString(code.hashCode());
        if (!entity.getValue().equals(hashedInput)) {
            return false;
        }

        verificationRepo.delete(entity);
        return true;
    }
}
