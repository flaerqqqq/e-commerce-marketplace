package com.example.ecommercemarketplace.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long inventory;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private MainProductImage mainProductImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;
}
