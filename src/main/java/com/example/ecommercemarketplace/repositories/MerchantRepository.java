package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN 'true' ELSE 'false' END FROM Merchant m WHERE m.emailConfirmationToken.token = :token")
    boolean existsByToken(@Param("token") String token);

    Optional<Merchant> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPublicId(String publicId);

    Optional<Merchant> findByEmailConfirmationToken(EmailConfirmationToken emailConfirmationToken);

    Optional<Merchant> findByPublicId(String publicId);

    void deleteByPublicId(String publicId);
}
