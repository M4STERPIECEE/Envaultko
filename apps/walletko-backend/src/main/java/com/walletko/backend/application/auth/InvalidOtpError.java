package com.walletko.backend.application.auth;

public class InvalidOtpError extends RuntimeException {
    public InvalidOtpError(String message) {
        super(message);
    }
}
