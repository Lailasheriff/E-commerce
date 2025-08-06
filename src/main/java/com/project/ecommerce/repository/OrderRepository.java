package com.project.ecommerce.repository;

import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderStatus;
import com.project.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyer(User buyer);

    List<Order> findByBuyerAndStatus(User buyer, OrderStatus status);
}
