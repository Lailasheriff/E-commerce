package com.project.ecommerce.repository;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.project.ecommerce.dto.ProductSummaryDTO(p.id, p.name, p.price, p.imageUrl) FROM Product p")
    Page<ProductSummaryDTO> findAllProductSummaries(Pageable pageable);

    List<Product> findAllByOrderByTotalOrdersAsc();
    List<Product> findAllByOrderByTotalOrdersDesc();

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Integer getTotalQuantitySoldByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(DISTINCT o.id) FROM Order o JOIN o.items oi WHERE oi.product.id = :productId")
    Integer getTotalOrdersByProductId(@Param("productId") Long productId);

    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) " +
            "FROM OrderItem oi WHERE oi.product.seller.id = :sellerId " +
            "GROUP BY oi.product.id, oi.product.name ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> getProductSalesStats(@Param("sellerId") Long sellerId);
}
