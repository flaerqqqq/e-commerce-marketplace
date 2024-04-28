package com.example.ecommercemarketplace.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartResponseDto {

    private Long id;

    private List<CartItemResponseDto> cartItems;
}
