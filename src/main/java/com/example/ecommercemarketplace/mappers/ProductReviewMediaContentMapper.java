package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.ProductReviewMediaContentDto;
import com.example.ecommercemarketplace.dto.ProductReviewMediaContentResponseDto;
import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import com.example.ecommercemarketplace.repositories.ProductReviewRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductReviewMediaContentMapper {

    @Autowired
    protected ProductReviewRepository productReviewRepository;

    @Mapping(source = "productReview.id", target = "productReviewId")
    @Mapping(source = "mediaContentType", target = "mediaContentType")
    public abstract ProductReviewMediaContentDto mapTo(ProductReviewMediaContent productReviewMediaContent);

    @Mapping(target = "productReview", expression = "java(mediaContentDto.getProductReviewId() != null ?" +
            "productReviewRepository.findById(mediaContentDto.getProductReviewId()).get() :" +
            "null)")
    @Mapping(source = "mediaContentType", target = "mediaContentType")
    public abstract ProductReviewMediaContent mapFrom(ProductReviewMediaContentDto mediaContentDto);

    @Mapping(source = "mediaContentType", target = "mediaContentType")
    public abstract ProductReviewMediaContentResponseDto mapToResponseDto(ProductReviewMediaContent productReviewMediaContent);
}
