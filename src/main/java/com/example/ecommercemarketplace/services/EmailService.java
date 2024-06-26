package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.UserDto;

public interface EmailService {

    void sendMessageWithVerificationCode(String toEmail, String code);

    void sendMessageWithPasswordResetCode(String toEmail, String code);

    void sendMail(String to, String subject, String body);

    void sendSuccessfulRegistrationMessage(UserDto userDto);

    void sendSuccessfulMerchantRegistrationMessage(MerchantDto merchantDto);
}
