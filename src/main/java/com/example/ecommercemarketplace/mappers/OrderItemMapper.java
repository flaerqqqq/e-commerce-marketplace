package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.OrderItemResponseDto;
import com.example.ecommercemarketplace.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);
}
