package com.project.ecommerce.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemResponse {

    private String productName;
    private int quantity;
    private double price;
}
