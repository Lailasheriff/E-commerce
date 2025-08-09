package com.project.ecommerce.service;

import com.project.ecommerce.dto.ReviewRequest;
import com.project.ecommerce.entity.OrderStatus;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.Review;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.exception.ReviewNotAllowedException;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.ReviewRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void submitReview(Long buyerId, ReviewRequest reviewRequest) {

        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));

        boolean delivered = orderRepository.existsByBuyerAndStatusAndItemsProduct(buyer, OrderStatus.DELIVERED, product);

        if (!delivered) {
            throw new ReviewNotAllowedException("Buyer has not purchased this product or it has not been delivered yet");
        }

        if (reviewRepository.existsByUserAndProduct(buyer, product)) {
            throw new ReviewNotAllowedException("Buyer has already submitted a review for this product");
        }

        // Create and save the review
        Review review = new Review();
        review.setUser(buyer);
        review.setProduct(product);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        reviewRepository.save(review);
    }
}
