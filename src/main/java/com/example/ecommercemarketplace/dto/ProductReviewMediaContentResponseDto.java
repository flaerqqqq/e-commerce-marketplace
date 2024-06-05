package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewMediaContentResponseDto {

    private Long id;

    private ProductReviewMediaContent.MediaContentType mediaContentType;

    private String url;
}
