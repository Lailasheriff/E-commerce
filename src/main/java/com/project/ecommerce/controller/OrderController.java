package com.project.ecommerce.controller;


import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.service.JwtService;
import com.project.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            // extract user ID from JWT in request header
            Long userId = extractUserIdFromRequest(request);

            // process the checkout
            orderService.checkout(userId);

            return ResponseEntity.ok("Checkout successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during checkout: " + e.getMessage());
        }
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // Validate the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        // Extract the token from the header and remove "Bearer " prefix
        String token = authHeader.substring(7);

        // Validate the JWT token
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }

        return jwtService.extractId(token);
    }
}
