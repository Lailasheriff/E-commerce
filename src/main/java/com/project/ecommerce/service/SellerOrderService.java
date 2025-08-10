package com.project.ecommerce.service;
import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.dto.SellerProductDetailsDTO;

import java.util.List;

public interface SellerOrderService {

    SellerProductDetailsDTO getProductDetailsById(Long productId);

    List<OrderDTO> getOrdersBySellerId(Long sellerId);

    String updateOrderStatus(Long orderId, String status);

    List<SellerProductDetailsDTO> getProductsStatsSorted(Long sellerId, String sortBy);
}
