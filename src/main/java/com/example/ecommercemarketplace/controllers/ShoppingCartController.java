package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.CartItemRequestDto;
import com.example.ecommercemarketplace.dto.ShoppingCartResponseDto;
import com.example.ecommercemarketplace.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/cart-items")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addItemToShoppingCart(Authentication authentication,
                                                         @RequestBody CartItemRequestDto cartItemRequestDto){
        return shoppingCartService.addItemToShoppingCart(authentication, cartItemRequestDto);
    }

}
