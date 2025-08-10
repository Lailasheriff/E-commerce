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
public class OrderDTO {

    private Long orderId;
    private LocalDateTime orderDate;
    List<OrderItemDTO> orderItems;
    private OrderStatus orderStatus;
    private String buyerName;
}
