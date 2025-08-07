package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ReviewRequest;
import com.project.ecommerce.entity.Review;
import com.project.ecommerce.service.JwtService;
import com.project.ecommerce.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer")
public class ReviewController {

    private JwtService jwtService;
    private ReviewService reviewService;

    @Autowired
    public ReviewController(JwtService jwtService, ReviewService reviewService) {
        this.jwtService = jwtService;
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest reviewRequest, HttpServletRequest request) {

        try {
            // Extract user ID from JWT in request header
            Long buyerId = extractUserIdFromRequest(request);

            // Submit the review
            reviewService.submitReview(buyerId, reviewRequest);

            return ResponseEntity.ok("Review submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting review: " + e.getMessage());
        }
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // Validate the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        // Extract the token from the header and remove "Bearer " prefix
        String token = authHeader.substring(7);

        // Validate the JWT token
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }

        return jwtService.extractId(token);
    }
}
