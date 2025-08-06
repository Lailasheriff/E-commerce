package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Method to find products by seller ID
    List<Product> findBySellerId(Long sellerId);
    // Method to find all products for a seller
    List<Product> findAllBySellerId(Long sellerId);



}
