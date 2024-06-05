package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(uses = {ProductReviewMapper.class})
public interface UserMapper {

    @Mapping(target = "productReviews", ignore = true)
    @Mapping(source = "enabled", target = "isEnabled")
    UserEntity mapFrom(UserDto userDto);

    @Mapping(source = "enabled", target = "isEnabled")
    UserDto mapTo(UserEntity userEntity);
}
