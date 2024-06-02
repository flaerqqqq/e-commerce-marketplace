package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.ProductReviewDto;
import com.example.ecommercemarketplace.dto.ProductReviewResponseDto;
import com.example.ecommercemarketplace.models.ProductReview;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.repositories.ProductReviewRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ProductReviewMediaContentMapper.class})
public abstract class ProductReviewMapper {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.id", target = "userId")
    public abstract ProductReviewDto mapTo(ProductReview productReview);

    @Mapping(target = "product", expression = "java(productReviewDto.getProductId() != null ?" +
            "productRepository.findById(productReviewDto.getProductId()).get() : null)")
    @Mapping(target = "user", expression = "java(productReviewDto.getUserId() != null ?" +
            "userRepository.findById(productReviewDto.getProductId()).get() : null)")
    public abstract ProductReview mapFrom(ProductReviewDto productReviewDto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.id", target = "userId")
    public abstract ProductReviewResponseDto mapToResponseDto(ProductReview productReview);


}
