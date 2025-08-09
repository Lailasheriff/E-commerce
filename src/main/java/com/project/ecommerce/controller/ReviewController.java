package com.project.ecommerce.controller;

import com.project.ecommerce.dto.ReviewRequest;
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

        // Extract user ID from JWT in request header
        Long buyerId = jwtService.extractUserIdFromRequest(request);

        // Submit the review
        reviewService.submitReview(buyerId, reviewRequest);

        return ResponseEntity.ok("Review submitted successfully!");
    }
}
