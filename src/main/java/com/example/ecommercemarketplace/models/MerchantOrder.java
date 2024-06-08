package com.example.ecommercemarketplace.models;

import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "merchant_orders")
public class MerchantOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private MerchantOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "parent_order_id")
    private Order parentOrder;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @OneToMany(mappedBy = "merchantOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;
}
