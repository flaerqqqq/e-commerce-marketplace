package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.MerchantResponseDto;
import com.example.ecommercemarketplace.models.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ProductReviewMapper.class})
public interface MerchantMapper {

    @Mapping(source = "enabled", target = "isEnabled")
    Merchant mapFrom(MerchantDto merchantDto);

    @Mapping(source = "enabled", target = "isEnabled")
    MerchantDto mapTo(Merchant merchant);

    MerchantResponseDto toResponseDto(MerchantDto merchantDto);
}
