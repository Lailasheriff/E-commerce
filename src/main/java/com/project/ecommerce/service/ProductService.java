package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    Page<ProductSummaryDTO> getAllProductSummaries(Pageable pageable);
    ProductDetailsDTO getProductDetailsWithReviews(Long productId);

}
