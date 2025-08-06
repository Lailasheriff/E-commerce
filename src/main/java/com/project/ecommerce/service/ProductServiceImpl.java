package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.dto.ReviewDTO;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.Review;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Page<ProductSummaryDTO> getAllProductSummaries(Pageable pageable) {
        return productRepository.findAllProductSummaries(pageable);
    }

    @Override
    public ProductDetailsDTO getProductDetailsWithReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<Review> reviews = reviewRepository.findByProductId(productId);
        return toProductDetailsDTO(product, reviews);
    }

    private ProductDetailsDTO toProductDetailsDTO(Product product, List<Review> reviews) {
        ProductDetailsDTO dto = new ProductDetailsDTO();
        double averageRating = 0.0;
        if (!reviews.isEmpty()) {
            averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        }
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantityAvailable(product.getQuantity());
        dto.setImageUrl(product.getImageUrl());
        dto.setTotalReviews(reviews.size());
        dto.setAverageRating(averageRating);
        dto.setReviews(reviews.stream().map(this::toReviewDTO).collect(Collectors.toList()));
        return dto;
    }

    private ReviewDTO toReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setReviewerName(review.getUser().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
