package com.example.ecommercemarketplace.repositories;

import com.example.ecommercemarketplace.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
