package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.dto.ProductSummary;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    // Method to add a product for a seller
    public void addProduct(Long sellerId, ProductRequest productRequest) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        try{
            Product product = new Product(productRequest.getName(),
                                          productRequest.getDescription(),
                                          productRequest.getPrice(),
                                          productRequest.getQuantity(),
                                          seller,
                                          productRequest.getImageUrl()
                                            );
            productRepository.save(product);
        }catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    // Method to update a product for a seller
    public void updateProduct(Long sellerId, Long productId, ProductRequest productRequest) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!(product.getSeller().getId() == (seller.getId()))) {
            throw new RuntimeException("You are not authorized to update this product");
        }

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setImageUrl(productRequest.getImageUrl());

        productRepository.save(product);
    }

    // Method to delete a product for a seller
    public void deleteProduct(Long sellerId, Long productId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!(product.getSeller().getId() == (seller.getId()))) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        productRepository.delete(product);
    }

    // Method to get all products for a seller
    public List<ProductSummary> getProductsBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        List<Product> products = productRepository.findAllBySellerId(sellerId);
        return products.stream()
                .map(product -> new ProductSummary(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        3.5)) // Placeholder for average rating, can be replaced with actual logic))
                .toList();
    }
}
