package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.service.SellerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerProductController {
    @Autowired
    SellerProductService sellerProductService;
    // method for adding products
    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        // need to add validation here for product details
        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            sellerProductService.addProduct(sellerId, productRequest);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
        }
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/update-product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            sellerProductService.updateProduct(sellerId, productId, productRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
        }
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            sellerProductService.deleteProduct(sellerId, productId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
        }
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/get-products")
    public ResponseEntity<?> getProducts() {
        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(sellerProductService.getProductsBySeller(sellerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching products: " + e.getMessage());
        }
    }


}
