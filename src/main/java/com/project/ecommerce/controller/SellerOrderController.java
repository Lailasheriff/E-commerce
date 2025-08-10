package com.project.ecommerce.controller;
import com.project.ecommerce.dto.OrderResponse;
import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.service.SellerOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;

    public SellerOrderController(SellerOrderService sellerOrderService) {
        this.sellerOrderService = sellerOrderService;
    }

    // view product details
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductDetails(@PathVariable("productId") Long productId) {

        ProductDetailsDTO productDetails = sellerOrderService.getProductDetailsById(productId);
        return ResponseEntity.ok(productDetails);
    }


    // view orders history for seller
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getSellerOrders() {

        Long sellerId = 1L;
        List<OrderResponse> orders = sellerOrderService.getOrdersBySellerId(sellerId);
        return ResponseEntity.ok(orders);
    }


    // update order status
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                    @RequestParam("status") String status) {

        String result = sellerOrderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(result);
    }


    // filter by most/least products for seller
    @GetMapping("/products/stats")
    public ResponseEntity<List<ProductDetailsDTO>> getProductsBySalesCount(
            @RequestParam(value = "sort", defaultValue = "desc") String sortOrder) {

        Long sellerId = 1L;
        List<ProductDetailsDTO> products = sellerOrderService.getProductsStatsSorted(sellerId, sortOrder);
        return ResponseEntity.ok(products);
    }

}
