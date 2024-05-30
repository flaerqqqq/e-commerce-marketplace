package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MainProductImageDto;
import com.example.ecommercemarketplace.models.MainProductImage;
import org.mapstruct.Mapper;

@Mapper
public interface MainProductImageMapper {

    MainProductImage mapFrom(MainProductImageDto mainProductImageDto);

    MainProductImageDto mapTo(MainProductImage mainProductImage);
}
