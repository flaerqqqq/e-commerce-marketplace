package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.MerchantOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantOrderRepository extends JpaRepository<MerchantOrder, Long> {

    Page<MerchantOrder> findAllByMerchant(Merchant merchant, Pageable pageable);
}
