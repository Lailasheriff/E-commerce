package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.dto.OrderItemResponse;
import com.project.ecommerce.dto.OrderResponse;
import com.project.ecommerce.dto.ProductDetailsDTO;

import java.util.List;

public interface SellerOrderService {

    ProductDetailsDTO getProductDetailsById(Long productId);

    List<OrderDTO> getOrdersBySellerId(Long sellerId);

    String updateOrderStatus(Long orderId, String status);

    List<ProductDetailsDTO> getProductsStatsSorted(Long sellerId, String sortBy);
}
