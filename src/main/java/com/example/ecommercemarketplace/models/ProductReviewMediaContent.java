package com.example.ecommercemarketplace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_review_media_contents")
public class ProductReviewMediaContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_review_id")
    private ProductReview productReview;

    @Enumerated(EnumType.STRING)
    private MediaContentType mediaContentType;

    @Column(nullable = false)
    private String url;

    public enum MediaContentType {
        IMAGE,
        VIDEO
    }
}
