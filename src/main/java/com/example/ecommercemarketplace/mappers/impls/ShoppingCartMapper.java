package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.CartItemDto;
import com.example.ecommercemarketplace.dto.ShoppingCartDto;
import com.example.ecommercemarketplace.dto.ShoppingCartResponseDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.CartItem;
import com.example.ecommercemarketplace.models.ShoppingCart;
import com.example.ecommercemarketplace.models.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ShoppingCartMapper implements Mapper<ShoppingCart, ShoppingCartDto> {

    private final CartItemMapper cartItemMapper;
    private final Mapper<UserEntity, UserDto> userMapper;

    @Override
    public ShoppingCart mapFrom(ShoppingCartDto shoppingCartDto) {
        return null;
    }

    @Override
    public ShoppingCartDto mapTo(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()
                .id(shoppingCart.getId())
                .cartItems(shoppingCart.getCartItems().stream()
                        .map(cartItemMapper::mapTo)
                        .collect(Collectors.toList()))
                .user(userMapper.mapTo(shoppingCart.getUser()))
                .build();
    }

    public ShoppingCartResponseDto mapToResponseDto(ShoppingCart shoppingCart){
        return ShoppingCartResponseDto.builder()
                .id(shoppingCart.getId())
                .cartItems(shoppingCart.getCartItems().stream()
                        .map(cartItemMapper::mapToResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
