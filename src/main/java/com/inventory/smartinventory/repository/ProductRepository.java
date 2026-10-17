package com.inventory.smartinventory.repository;

import com.inventory.smartinventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}