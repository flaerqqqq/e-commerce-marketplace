package com.example.ecommercemarketplace.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;

    private ProductDto product;

    private int quantity;
}
