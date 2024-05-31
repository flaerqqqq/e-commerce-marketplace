package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.MainProductImageDto;
import com.example.ecommercemarketplace.dto.MainProductImageResponseDto;
import com.example.ecommercemarketplace.dto.ProductImageDto;
import com.example.ecommercemarketplace.models.MainProductImage;
import com.example.ecommercemarketplace.models.ProductImage;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.services.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {ProductMapper.class})
public abstract class MainProductImageMapper {

    @Autowired
    protected ProductRepository productRepository;

    @Mapping(target = "product",
            expression = "java(productRepository.findById(mainProductImageDto.getProductId()).get())")
    public abstract MainProductImage mapFrom(MainProductImageDto mainProductImageDto);

    @Mapping(source = "product.id", target = "productId")
    public abstract MainProductImageDto mapTo(MainProductImage mainProductImage);

    public abstract MainProductImageResponseDto mapToResponseDto(MainProductImageDto mainProductImageDto);
}
