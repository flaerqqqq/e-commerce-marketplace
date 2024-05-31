package com.example.ecommercemarketplace.services;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.ecommercemarketplace.models.MainProductImage;
import com.example.ecommercemarketplace.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageBucketService {

    S3ObjectInputStream findByName(String fileName);

    List<ProductImage> saveImages(final List<MultipartFile> images);

    MainProductImage saveMainImage(final MultipartFile mainImage);
}
