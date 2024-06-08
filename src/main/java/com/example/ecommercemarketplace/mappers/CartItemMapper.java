package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.CartItemDto;
import com.example.ecommercemarketplace.dto.CartItemResponseDto;
import com.example.ecommercemarketplace.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ProductMapper.class})
public interface CartItemMapper {

    CartItem mapFrom(CartItemDto cartItemDto);

    CartItemDto mapTo(CartItem cartItem);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    CartItemResponseDto mapToResponseDto(CartItem cartItem);
}
