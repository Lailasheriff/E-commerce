package com.project.ecommerce.repository;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.OrderItem;
import com.project.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi " +
            "WHERE oi.product.id = :productId AND oi.order.status = com.project.ecommerce.entity.OrderStatus.DELIVERED")
    long countShippedItemsByProductId(@Param("productId") Long productId);




}
