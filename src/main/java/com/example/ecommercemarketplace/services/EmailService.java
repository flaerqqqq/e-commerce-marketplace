package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.UserDto;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendMessageWithVerificationCode(String toEmail, String code);

    void sendMessageWithPasswordResetCode(String toEmail, String code);

    void sendMail(String to, String subject, String body);

    void sendSuccessfulRegistrationMessage(UserDto userDto);
}
