package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.ShoppingCart;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    boolean existsByUser(UserEntity user);

    Optional<ShoppingCart> findByUser(UserEntity user);
}
