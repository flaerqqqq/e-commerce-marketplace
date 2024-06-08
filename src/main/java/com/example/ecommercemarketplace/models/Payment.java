package com.example.ecommercemarketplace.models;


import com.example.ecommercemarketplace.models.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod method;

    @OneToOne(mappedBy = "payment")
    private Order order;
}









