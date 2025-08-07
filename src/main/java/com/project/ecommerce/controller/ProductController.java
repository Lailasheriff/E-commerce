package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    public Page<ProductSummaryDTO> getAllProducts(@PageableDefault(size = 10) Pageable pageable)
    {
        return productService.getAllProductSummaries(pageable);
    }

    @GetMapping("/products/filter")
    public ResponseEntity<List<ProductDetailsDTO>> getAllProductDetailsSorted(
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        List<ProductDetailsDTO> products = productService.getAllProductDetailsSorted(sortBy, order);
        return ResponseEntity.ok(products);
    }





    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductDetailsWithReviews(@PathVariable Long productId) {
        ProductDetailsDTO productDetails = productService.getProductDetailsWithReviews(productId);
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDetailsDTO>> searchProducts(@RequestParam String query) {
        List<ProductDetailsDTO> results = productService.searchProducts(query);
        return ResponseEntity.ok(results);
    }


}
