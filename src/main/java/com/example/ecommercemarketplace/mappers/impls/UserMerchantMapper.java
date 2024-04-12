package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMerchantMapper implements Mapper<UserDto, MerchantDto> {

    private ModelMapper modelMapper;

    @Override
    public UserDto mapFrom(MerchantDto merchantDto) {
        return modelMapper.map(merchantDto, UserDto.class);
    }

    @Override
    public MerchantDto mapTo(UserDto userDto) {
        return modelMapper.map(userDto, MerchantDto.class);
    }
}
