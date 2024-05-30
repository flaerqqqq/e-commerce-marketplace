package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.ShoppingCartDto;
import com.example.ecommercemarketplace.dto.ShoppingCartResponseDto;
import com.example.ecommercemarketplace.models.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(uses = {CartItemMapper.class, UserMapper.class})
public interface ShoppingCartMapper {


    ShoppingCart mapFrom(ShoppingCartDto shoppingCartDto);

    ShoppingCartDto mapTo(ShoppingCart shoppingCart);

    ShoppingCartResponseDto mapToResponseDto(ShoppingCart shoppingCart);
}
