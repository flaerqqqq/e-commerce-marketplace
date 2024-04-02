package com.example.ecommercemarketplace.services;

public interface EmailService {

    void sendMessageWithVerificationCode(String toEmail, String code);

    void sendMail(String to, String subject, String body);
}
