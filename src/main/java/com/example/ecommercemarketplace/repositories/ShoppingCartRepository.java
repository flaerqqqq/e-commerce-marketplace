package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
