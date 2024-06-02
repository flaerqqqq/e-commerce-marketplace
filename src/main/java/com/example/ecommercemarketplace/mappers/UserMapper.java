package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.models.UserEntity;
import org.mapstruct.Mapper;

@Mapper(uses = {ProductReviewMapper.class})
public interface UserMapper {

    UserEntity mapFrom(UserDto userDto);

    UserDto mapTo(UserEntity userEntity);
}
