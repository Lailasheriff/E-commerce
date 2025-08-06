package com.project.ecommerce.repository;

import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.project.ecommerce.dto.ProductSummaryDTO(p.id, p.name, p.price, p.imageUrl) FROM Product p")
    Page<ProductSummaryDTO> findAllProductSummaries(Pageable pageable);
}
