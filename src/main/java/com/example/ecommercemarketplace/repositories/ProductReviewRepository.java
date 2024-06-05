package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.models.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    Page<ProductReview> findAllByProduct(Product product, Pageable pageable);
}
