package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByMerchant(Merchant merchant, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);
}
