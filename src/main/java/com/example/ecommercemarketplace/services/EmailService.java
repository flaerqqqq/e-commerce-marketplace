package com.example.ecommercemarketplace.services;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendMessageWithVerificationCode(String toEmail, String code) throws MessagingException;

    void sendMessageWithPasswordResetCode(String toEmail, String code) throws MessagingException;

    void sendMail(String to, String subject, String body) throws MessagingException;
}
