package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    boolean existsByToken(String token);

    Optional<EmailConfirmationToken> findByToken(String token);

}
