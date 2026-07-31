package com.walletko.backend.domain.auth;

import java.util.Optional;

public interface OtpPort {
    void sendOtp(String email);
    boolean verifyOtp(String email, String code);
}
