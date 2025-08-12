package com.project.ecommerce.repository;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new com.project.ecommerce.dto.ProductSummaryDTO(p.id, p.name, p.price, p.imageUrl) FROM Product p")
    Page<ProductSummaryDTO> findAllProductSummaries(Pageable pageable);

    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN FETCH p.reviews r
    WHERE 
        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR CAST(p.price AS string) LIKE CONCAT('%', :keyword, '%')
        OR CAST(p.quantity AS string) LIKE CONCAT('%', :keyword, '%')
        OR CAST(p.id AS string) LIKE CONCAT('%', :keyword, '%')
        """)
    Page<Product> search(@Param("keyword") String keyword, Pageable pageable);

    List<Product> findBySellerId(Long sellerId);
    List<Product> findAllBySellerId(Long sellerId);


    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Integer getTotalQuantitySoldByProductId(@Param("productId") Long productId);


    @Query("SELECT COUNT(DISTINCT o.id) FROM Order o JOIN o.items oi WHERE oi.product.id = :productId")
    Integer getTotalOrdersByProductId(@Param("productId") Long productId);


    /*@Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) " +
            "FROM OrderItem oi WHERE oi.product.seller.id = :sellerId " +
            "GROUP BY oi.product.id, oi.product.name")
    List<Object[]> getProductSalesStats(@Param("sellerId") Long sellerId);*/

    @Query(value = "SELECT oi.product_id, p.name, SUM(oi.quantity) as q " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "WHERE p.seller_id = :sellerId " +
            "GROUP BY oi.product_id, p.name",
            nativeQuery = true)
    List<Object[]> getProductSalesStats(@Param("sellerId") Long sellerId);

}
