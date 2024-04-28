package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
