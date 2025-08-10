package com.project.ecommerce.controller;

import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.service.CartService;
import com.project.ecommerce.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/buyer/cart")
public class CartController {

    private CartService cartService;
    private JwtService jwtService;

    @Autowired
    public CartController(CartService cartService, JwtService jwtService) {
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            // extract user ID from JWT in request header
            Long userId = jwtService.extractUserIdFromRequest(request);

            // get cart items for the user
            List<CartItemResponse> cartItems = cartService.getCartItemsResponse(userId);

            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding item to cart: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        try {
            // extract user ID from JWT in request header
            Long userId = jwtService.extractUserIdFromRequest(request);

            // add item to cart
            cartService.addToCart(userId, cartItemRequest);

            return ResponseEntity.ok("Item added to cart successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding item to cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId, HttpServletRequest request) {
        try {
            // extract user ID from JWT in request header
            Long userId = jwtService.extractUserIdFromRequest(request);

            // remove cart item for user
            cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok("Item removed from cart successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing item from cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        try {
            // extract user ID from JWT in request header
            Long userId = jwtService.extractUserIdFromRequest(request);

            // clear all cart items for the user
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error clearing cart: " + e.getMessage());
        }
    }
}
