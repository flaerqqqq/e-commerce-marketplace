package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
