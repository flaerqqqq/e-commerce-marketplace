package com.example.ecommercemarketplace.services;


import com.example.ecommercemarketplace.dto.ProductReviewRequestDto;
import com.example.ecommercemarketplace.dto.ProductReviewResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductReviewService {

    ProductReviewResponseDto createProductReview(Long productId, ProductReviewRequestDto createRequest,
                                                 List<MultipartFile> mediaContent, Authentication authentication);
}
