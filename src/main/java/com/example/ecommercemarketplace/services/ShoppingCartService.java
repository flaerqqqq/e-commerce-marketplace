package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    ShoppingCartResponseDto addItemToShoppingCart(Authentication authentication, CartItemRequestDto cartItem);

    Page<CartItemDto> findAllItemsByShoppingCart(ShoppingCartDto shoppingCart);
}
