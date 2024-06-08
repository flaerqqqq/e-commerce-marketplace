package com.example.ecommercemarketplace.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}