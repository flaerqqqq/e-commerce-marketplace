package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ProductReviewMapper.class})
public interface UserMapper {

    @Mapping(source = "enabled", target = "isEnabled")
    UserEntity mapFrom(UserDto userDto);

    @Mapping(source = "enabled", target = "isEnabled")
    UserDto mapTo(UserEntity userEntity);
}
