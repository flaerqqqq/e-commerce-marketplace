package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.CartItemDto;
import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.CartItem;
import com.example.ecommercemarketplace.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartItemMapper implements Mapper<CartItem, CartItemDto> {

    private final Mapper<Product, ProductDto> productMapper;

    @Override
    public CartItem mapFrom(CartItemDto cartItemDto) {
        return CartItem.builder()
                .id(cartItemDto.getId())
                .product(productMapper.mapFrom(cartItemDto.getProduct()))
                .quantity(cartItemDto.getQuantity())
                .build();

    }

    @Override
    public CartItemDto mapTo(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .product(productMapper.mapTo(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .build();
    }
}
