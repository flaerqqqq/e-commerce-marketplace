package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Merchant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MerchantMapper implements Mapper<Merchant, MerchantDto> {

    private final ModelMapper modelMapper;

    @Override
    public Merchant mapFrom(MerchantDto merchantDto) {
        return modelMapper.map(merchantDto, Merchant.class);
    }

    @Override
    public MerchantDto mapTo(Merchant merchant) {
        return modelMapper.map(merchant, MerchantDto.class);
    }
}
