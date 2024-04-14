package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Product;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper implements Mapper<Product, ProductDto> {

    private final ModelMapper modelMapper;

    @Override
    public Product mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    @Override
    public ProductDto mapTo(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
