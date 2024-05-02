package com.example.ecommercemarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemQuantityUpdateRequest {

    private int quantity;
}
