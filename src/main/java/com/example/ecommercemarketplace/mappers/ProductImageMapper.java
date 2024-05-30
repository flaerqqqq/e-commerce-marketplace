package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.ProductImageDto;
import com.example.ecommercemarketplace.models.ProductImage;
import org.mapstruct.Mapper;

@Mapper
public interface ProductImageMapper {

    ProductImage mapFrom(ProductImageDto productImageDto);

    ProductImageDto mapTo(ProductImage productImage);
}
