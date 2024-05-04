package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Order;
import com.example.ecommercemarketplace.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUser(UserEntity user, Pageable pageable);
}
