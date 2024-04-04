package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPublicId(String publicId);

    Optional<UserEntity> findByEmailConfirmationToken(EmailConfirmationToken emailConfirmationToken);

    Optional<UserEntity> findByPublicId(String publicId);

    void deleteByPublicId(String publicId);


}
