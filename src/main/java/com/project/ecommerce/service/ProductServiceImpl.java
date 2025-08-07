package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductSummaryDTO> getAllProductSummaries(Pageable pageable) {
        return productRepository.findAllProductSummaries(pageable);
    }
}
