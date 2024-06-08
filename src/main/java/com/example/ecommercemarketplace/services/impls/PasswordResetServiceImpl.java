package com.example.ecommercemarketplace.services.impls;


import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.PasswordResetRequestDto;
import com.example.ecommercemarketplace.events.EmailChangeEvent;
import com.example.ecommercemarketplace.exceptions.PasswordResetTokenNotFoundException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.MerchantMapper;
import com.example.ecommercemarketplace.mappers.UserMapper;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.PasswordResetToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.PasswordResetTokenRepository;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.EmailService;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.PasswordResetService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserService userService;
    private final MerchantService merchantService;
    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void requestPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {
        String email = passwordResetRequestDto.getEmail();

        if (!userService.existsByEmail(email)) {
            throw new UserNotFoundException("User with email=%s is not found".formatted(email));
        }

        PasswordResetToken passwordResetToken;
        UserEntity user = userMapper.mapFrom(userService.findByEmail(email));

        String tokenValue = jwtService.generatePasswordResetToken(email);
        if (passwordResetTokenRepository.existsByUser(user)) {
            passwordResetToken = passwordResetTokenRepository.findByUser(user).get();
            passwordResetToken.setToken(tokenValue);
        } else {
            passwordResetToken = PasswordResetToken.builder()
                    .token(tokenValue)
                    .user(user)
                    .build();
        }

        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendMessageWithPasswordResetCode(email, tokenValue);
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirmationRequestDto passwordResetConfirmationRequestDto) {
        String token = passwordResetConfirmationRequestDto.getToken();

        jwtService.isValid(token);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() ->
                new PasswordResetTokenNotFoundException("Password reset token is not found"));

        String newPassword = passwordEncoder.encode(passwordResetConfirmationRequestDto.getPassword());
        UserEntity user = passwordResetToken.getUser();
        user.setPassword(newPassword);

        updateUserMerchant(user);

        publishEvent(user);

        passwordResetTokenRepository.delete(passwordResetToken);
    }

    private void publishEvent(UserEntity user) {
        eventPublisher.publishEvent(new EmailChangeEvent(this, user));
    }

    private void updateUserMerchant(UserEntity user) {
        if (user instanceof Merchant merchant) {
            merchantService.updateMerchant(merchantMapper.mapTo(merchant));
        } else {
            userService.updateUser(userMapper.mapTo(user));
        }
    }

    @Override
    public void deleteExpiredTokens() {
        List<PasswordResetToken> tokens = passwordResetTokenRepository.findAll();

        for (var token : tokens) {
            try {
                jwtService.isValid(token.getToken());
            } catch (Exception e) {
                passwordResetTokenRepository.delete(token);
            }
        }
    }
}
