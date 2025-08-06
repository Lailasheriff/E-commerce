package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {
    Page<ProductSummaryDTO> getAllProductSummaries(Pageable pageable);
    ProductDetailsDTO getProductDetailsWithReviews(Long productId);
    List<ProductDetailsDTO> searchProducts(String query);
}
