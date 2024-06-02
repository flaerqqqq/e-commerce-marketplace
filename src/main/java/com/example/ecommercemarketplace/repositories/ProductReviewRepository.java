package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
}
