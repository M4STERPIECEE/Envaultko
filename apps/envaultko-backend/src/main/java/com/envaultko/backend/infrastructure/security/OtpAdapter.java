package com.envaultko.backend.infrastructure.security;

import com.envaultko.backend.domain.auth.OtpPort;
import org.springframework.stereotype.Component;

@Component
public class OtpAdapter implements OtpPort {

    private final OtpService otpService;

    public OtpAdapter(OtpService otpService) {
        this.otpService = otpService;
    }

    @Override
    public void sendOtp(String email) {
        otpService.sendOtp(email);
    }

    @Override
    public boolean verifyOtp(String email, String code) {
        return otpService.verifyOtp(email, code);
    }
}
