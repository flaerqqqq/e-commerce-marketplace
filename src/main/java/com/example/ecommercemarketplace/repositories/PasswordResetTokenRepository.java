package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.PasswordResetToken;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    boolean existsByToken(String token);

    boolean existsByUser(UserEntity user);

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(UserEntity user);
}
