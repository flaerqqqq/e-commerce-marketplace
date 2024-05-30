package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMerchantMapper {

    UserDto mapFrom(MerchantDto merchantDto);

    MerchantDto mapTo(UserDto userDto);
}
