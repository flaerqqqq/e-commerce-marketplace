package com.example.ecommercemarketplace.services.impls;


import com.example.ecommercemarketplace.dto.PasswordResetRequest;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.PasswordResetToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.PasswordResetTokenRepository;
import com.example.ecommercemarketplace.services.EmailService;
import com.example.ecommercemarketplace.services.PasswordResetService;
import com.example.ecommercemarketplace.services.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailService emailService;

    @Override
    public boolean requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        String email = passwordResetRequest.getEmail();

        if (!userService.existsByEmail(email)){
            System.out.println("test");
            return false;
        }

        UserEntity user = userMapper.mapFrom(userService.findByEmail(email));

        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .build();
        passwordResetTokenRepository.save(passwordResetToken);

        try {
            emailService.sendMessageWithPasswordResetCode(email, tokenValue);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }

        return true;
    }
}
