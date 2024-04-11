package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.RefreshToken;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(UserEntity user);

    boolean existsByUser(UserEntity user);

    boolean existsByToken(String token);

    Optional<RefreshToken> findByToken(String token);

    void removeByToken(String token);


}
