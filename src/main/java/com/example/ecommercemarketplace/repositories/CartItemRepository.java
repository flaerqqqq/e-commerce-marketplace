package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.CartItem;
import com.example.ecommercemarketplace.models.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Page<CartItem> findByShoppingCart(ShoppingCart shoppingCart, Pageable pageable);
}
