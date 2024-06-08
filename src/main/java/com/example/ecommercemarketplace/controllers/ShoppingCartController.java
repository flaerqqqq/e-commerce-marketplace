package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.CartItemQuantityUpdateRequest;
import com.example.ecommercemarketplace.dto.CartItemRequestDto;
import com.example.ecommercemarketplace.dto.CartItemResponseDto;
import com.example.ecommercemarketplace.dto.ShoppingCartResponseDto;
import com.example.ecommercemarketplace.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/cart-items")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto addItemToShoppingCart(@RequestBody CartItemRequestDto cartItemRequestDto,
                                                         Authentication authentication) {
        return shoppingCartService.addItemToShoppingCart(authentication, cartItemRequestDto);
    }

    @GetMapping("/cart-items")
    @PreAuthorize("hasRole('USER')")
    public Page<CartItemResponseDto> getAllItemsByCart(Pageable pageable, Authentication authentication) {
        return shoppingCartService.getAllItemsByShoppingCart(authentication, pageable);
    }

    @DeleteMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItem(@PathVariable("id") Long id, Authentication authentication) {
        shoppingCartService.deleteCartItem(authentication, id);
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('USER')")
    public CartItemResponseDto updateCartItemQuantity(@PathVariable("id") Long id,
                                                      @RequestBody CartItemQuantityUpdateRequest updateRequest,
                                                      Authentication authentication) {
        return shoppingCartService.updateCartItemQuantity(authentication, id, updateRequest);
    }

}
