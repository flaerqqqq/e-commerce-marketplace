package com.example.ecommercemarketplace.services.impls;


import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequest;
import com.example.ecommercemarketplace.dto.PasswordResetRequest;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.PasswordResetToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.PasswordResetTokenRepository;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.EmailService;
import com.example.ecommercemarketplace.services.PasswordResetService;
import com.example.ecommercemarketplace.services.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailService emailService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        String email = passwordResetRequest.getEmail();

        if (!userService.existsByEmail(email)){
            System.out.println("test");
            return false;
        }

        UserEntity user = userMapper.mapFrom(userService.findByEmail(email));

        String tokenValue = jwtService.generatePasswordResetToken(email);
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

    @Override
    public boolean confirmPasswordReset(PasswordResetConfirmationRequest passwordResetConfirmationRequest) {
        String token = passwordResetConfirmationRequest.getToken();
        System.out.println(passwordResetConfirmationRequest);
        if (!jwtService.isValid(token)){
            return false;
        }

        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if(passwordResetToken.isEmpty()){
            return false;
        }

        String newPassword = passwordEncoder.encode(passwordResetConfirmationRequest.getPassword());
        UserEntity user = passwordResetToken.get().getUser();
        user.setPassword(newPassword);
        userService.updateUser(userMapper.mapTo(user));
        passwordResetTokenRepository.delete(passwordResetToken.get());

        return true;
    }

    @Override
    public void deleteExpiredTokens() {
        List<PasswordResetToken> tokens = passwordResetTokenRepository.findAll();

        for (var token : tokens){
            try {
                jwtService.isValid(token.getToken());
            } catch (Exception e){
                passwordResetTokenRepository.delete(token);
            }
        }
    }
}
