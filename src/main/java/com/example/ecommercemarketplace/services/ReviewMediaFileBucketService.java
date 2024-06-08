package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewMediaFileBucketService {

    List<ProductReviewMediaContent> uploadProductMediaContent(List<MultipartFile> mediaContent);
}
