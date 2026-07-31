package com.walletko.backend.infrastructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SmtpMailer {
    private final JavaMailSender mailSender;
    private final String from;

    public SmtpMailer(JavaMailSender mailSender,
                       @Value("${walletko.email.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendOtpEmail(String to, String otp) {
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Your Walletko sign-in code");
        message.setText("Your verification code is: " + otp + "\n\n"
                      + "This code expires in 5 minutes.\n"
                      + "If you didn't request this, please ignore this email.");
        mailSender.send(message);
    }
}
