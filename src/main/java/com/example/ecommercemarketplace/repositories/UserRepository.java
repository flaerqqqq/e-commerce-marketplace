package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.emailConfirmationToken.token = :token")
    boolean existsByToken(@Param("token") String token);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPublicId(String publicId);

    Optional<UserEntity> findByEmailConfirmationToken(EmailConfirmationToken emailConfirmationToken);

    Optional<UserEntity> findByPublicId(String publicId);

    void deleteByPublicId(String publicId);
}
