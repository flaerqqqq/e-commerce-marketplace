package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.repositories.EmailConfirmationTokenRepository;
import com.example.ecommercemarketplace.services.EmailConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Override
    public boolean existsByToken(String token) {
        return emailConfirmationTokenRepository.existsByToken(token);
    }
}
