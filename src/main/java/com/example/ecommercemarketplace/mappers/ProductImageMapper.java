package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.ProductImageDto;
import com.example.ecommercemarketplace.dto.ProductImageResponseDto;
import com.example.ecommercemarketplace.models.ProductImage;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.services.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {ProductMapper.class})
public abstract class ProductImageMapper {

    @Autowired
    protected ProductRepository productRepository;

    @Mapping(target = "product",
            expression = "java(productRepository.findById(productImageDto.getProductId()).get())")
    public abstract ProductImage mapFrom(ProductImageDto productImageDto);

    @Mapping(source = "product.id", target = "productId")
    public abstract ProductImageDto mapTo(ProductImage productImage);

    public abstract ProductImageResponseDto mapToResponseDto(ProductImageDto productImageDto);
}
