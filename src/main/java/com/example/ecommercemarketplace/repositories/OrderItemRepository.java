package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
