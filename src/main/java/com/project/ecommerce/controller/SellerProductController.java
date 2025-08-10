package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.exception.MissingFilterParameterException;
import com.project.ecommerce.exception.MissingSearchParameterException;
import com.project.ecommerce.service.SellerProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@RestController
@RequestMapping("/seller")
public class SellerProductController {
    @Autowired
    SellerProductService sellerProductService;

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .sorted(Comparator.comparing(FieldError::getField))
                    .map(err -> err.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid product data");
            return ResponseEntity.badRequest().body(errorMessage);
        }

        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            sellerProductService.addProduct(sellerId, productRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/update-product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .sorted(Comparator.comparing(FieldError::getField))
                    .map(err -> err.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid product data");
            return ResponseEntity.badRequest().body(errorMessage);
        }
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

    @GetMapping("/search-products")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false) String query, HttpServletRequest request) {
        if (!request.getParameterMap().containsKey("query")) {
            throw new MissingSearchParameterException("Missing required parameter: query");
        }
        Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(sellerProductService.searchProductsBySeller(sellerId, query));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error searching products: " + e.getMessage());
        }
    }

    @GetMapping("/filter-products")
    public ResponseEntity<?> filterProducts(
            @RequestParam(name="sortBy",required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String order,HttpServletRequest request
    ) {
        if (!request.getParameterMap().containsKey("sortBy")) {
            throw new MissingFilterParameterException("Missing required parameter: SortBy");
        }
            Long sellerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            try {
                return ResponseEntity.ok(sellerProductService.filterProductsBySeller(sellerId, sortBy, order));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error filtering products: " + e.getMessage());
            }
    }

}
