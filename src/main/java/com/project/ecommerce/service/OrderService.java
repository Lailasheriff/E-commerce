package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderResponse;
import com.project.ecommerce.entity.Order;


import java.util.List;

public interface OrderService {

    void checkout(Long buyerId);

    List<OrderResponse> getOrderHistory(Long buyerId);
}
