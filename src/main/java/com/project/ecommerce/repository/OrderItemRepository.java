package com.project.ecommerce.repository;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.OrderItem;
import com.project.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
