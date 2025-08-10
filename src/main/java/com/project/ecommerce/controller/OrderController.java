package com.project.ecommerce.controller;


import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.OrderResponse;
import com.project.ecommerce.service.JwtService;
import com.project.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyer")
public class OrderController {

    private OrderService orderService;
    private JwtService jwtService;

    @Autowired
    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(HttpServletRequest request) {

        // extract user ID from JWT in request header
        Long userId = jwtService.extractUserIdFromRequest(request);

        // process the checkout
        orderService.checkout(userId);

        return ResponseEntity.ok("Checkout successful!");
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrderHistory(HttpServletRequest request) {

        // extract user ID from JWT in request header
        Long userId = jwtService.extractUserIdFromRequest(request);

        // get order history for the user
        List<OrderResponse> orderHistory = orderService.getOrderHistory(userId);

        return ResponseEntity.ok(orderHistory);
    }
}
