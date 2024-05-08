package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MerchantOrderResponseDto;
import com.example.ecommercemarketplace.models.MerchantOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {OrderItemMapper.class})
public interface MerchantOrderMapper {

    @Mapping(source = "status", target = "status")
    @Mapping(source = "parentOrder.id", target = "parentOrderId")
    MerchantOrderResponseDto toResponseDto(MerchantOrder merchantOrder);

}
