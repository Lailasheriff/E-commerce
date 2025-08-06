package com.project.ecommerce.controller;

import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.service.CartService;
import com.project.ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtService jwtService;

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Getting cart items for user ID: " + userId);
            List<CartItemResponse> cartItems = cartService.getCartItemsResponse(userId);
            System.out.println("Found " + cartItems.size() + " cart items");
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            System.out.println("Error getting cart items: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Adding to cart - User ID: " + userId + ", Product ID: " + cartItemRequest.getProductId() + ", Quantity: " + cartItemRequest.getQuantity());
            cartService.addToCart(userId, cartItemRequest);
            System.out.println("Successfully added item to cart");
            return ResponseEntity.ok("Item added to cart successfully!");
        } catch (Exception e) {
            System.out.println("Error adding item to cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error adding item to cart: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Updating cart item - User ID: " + userId + ", Product ID: " + cartItemRequest.getProductId() + ", Quantity: " + cartItemRequest.getQuantity());
            cartService.updateCartItem(userId, cartItemRequest);
            System.out.println("Successfully updated cart item");
            return ResponseEntity.ok("Cart item updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating cart item: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error updating cart item: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId, HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Removing from cart - User ID: " + userId + ", Product ID: " + productId);
            cartService.removeFromCart(userId, productId);
            System.out.println("Successfully removed item from cart");
            return ResponseEntity.ok("Item removed from cart successfully!");
        } catch (Exception e) {
            System.out.println("Error removing item from cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error removing item from cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Clearing cart for user ID: " + userId);
            cartService.clearCart(userId);
            System.out.println("Successfully cleared cart");
            return ResponseEntity.ok("Cart cleared successfully!");
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error clearing cart: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("Getting cart count for user ID: " + userId);
            int count = cartService.getCartItemCount(userId);
            System.out.println("Cart count: " + count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.out.println("Error getting cart count: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Debug endpoint to test database connection
    @GetMapping("/debug")
    public ResponseEntity<?> debugCart(HttpServletRequest request) {
        try {
            Long userId = extractUserIdFromRequest(request);
            System.out.println("=== DEBUG CART FOR USER " + userId + " ===");
            
            // Test getting cart items
            List<CartItemResponse> cartItems = cartService.getCartItemsResponse(userId);
            System.out.println("Cart items found: " + cartItems.size());
            
            // Test getting cart count
            int count = cartService.getCartItemCount(userId);
            System.out.println("Cart count: " + count);
            
            return ResponseEntity.ok(Map.of(
                "userId", userId,
                "cartItemsCount", cartItems.size(),
                "totalQuantity", count,
                "cartItems", cartItems
            ));
        } catch (Exception e) {
            System.out.println("Debug error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Debug error: " + e.getMessage());
        }
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        
        String token = authHeader.substring(7); // Remove "Bearer "
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }
        
        return jwtService.extractId(token);
    }
}
