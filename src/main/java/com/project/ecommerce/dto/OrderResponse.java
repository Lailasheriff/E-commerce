package com.project.ecommerce.dto;

import com.project.ecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponse {

    private Long orderId;
    private LocalDateTime orderDate;
    List<OrderItemResponse> orderItems;
    private OrderStatus orderStatus;
    private String buyerName;

    public OrderResponse(Long id, String name, double v, LocalDateTime createdAt, List<OrderItemResponse> itemResponses, String buyerName) {
        this.orderId = id;
        this.orderDate = createdAt;
        this.orderItems = itemResponses;
        this.orderStatus = OrderStatus.PLACED;
        this.buyerName = buyerName;
    }
}
