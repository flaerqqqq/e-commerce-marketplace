package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.MerchantOrder;
import com.example.ecommercemarketplace.models.Order;
import com.example.ecommercemarketplace.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Page<OrderItem> findAllByMerchantOrderIn(List<MerchantOrder> merchantOrders, Pageable pageable);
}
