package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderStatus;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyer(User buyer);

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findByBuyerAndStatus(User buyer, OrderStatus status);

    boolean existsByBuyerAndStatusAndItemsProduct(User buyer,
                                                       OrderStatus status,
                                                       Product product);


    @Query(value = "SELECT DISTINCT o.* FROM `Order` o " +
            "JOIN OrderItem oi ON o.id = oi.order_id " +
            "JOIN Product p ON oi.product_id = p.id " +
            "WHERE p.seller_id = :sellerId",
            nativeQuery = true)
    List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);
}