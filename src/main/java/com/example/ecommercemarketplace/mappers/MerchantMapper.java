package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.models.Merchant;
import org.mapstruct.Mapper;

@Mapper
public interface MerchantMapper {

    Merchant mapFrom(MerchantDto merchantDto);

    MerchantDto mapTo(Merchant merchant);
}
