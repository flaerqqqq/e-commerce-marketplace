package com.example.ecommercemarketplace.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDto {

    private Long id;

    private UserDto user;

    private List<CartItemDto> cartItems;
}
