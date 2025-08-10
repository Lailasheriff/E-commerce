package com.project.ecommerce.controller;
import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.dto.SellerProductDetailsDTO;
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
    public ResponseEntity<SellerProductDetailsDTO> getProductDetails(@PathVariable("productId") Long productId) {

        SellerProductDetailsDTO productDetails = sellerOrderService.getProductDetailsById(productId);
        return ResponseEntity.ok(productDetails);
    }


    // view orders history for seller
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getSellerOrders(@RequestParam Long sellerId) {

        // Long sellerId = 1L;
        List<OrderDTO> orders = sellerOrderService.getOrdersBySellerId(sellerId);
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
    public ResponseEntity<List<SellerProductDetailsDTO>> getProductsBySalesCount(
            @RequestParam(value = "sort", defaultValue = "desc") String sortOrder) {

        Long sellerId = 1L;
        List<SellerProductDetailsDTO> products = sellerOrderService.getProductsStatsSorted(sellerId, sortOrder);
        return ResponseEntity.ok(products);
    }

}
