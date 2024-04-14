package com.example.ecommercemarketplace.models;

import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.models.enums.MerchantType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Merchant extends UserEntity {

    @Column(name = "website_url", unique = true)
    private String websiteUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private MerchantType type;

    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<Product> products;

}
