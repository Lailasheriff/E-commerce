package com.project.ecommerce.controller;


import com.project.ecommerce.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer")
public class ReviewController {

    private JwtService jwtService;

    @PostMapping("/review")
    public ResponseEntity<?> review(HttpServletRequest request) {
        try {
            // Extract user ID from JWT in request header
            Long userId = jwtService.extractUserIdFromRequest(request);

            // Logic to handle review submission goes here

            return ResponseEntity.ok("Review submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting review: " + e.getMessage());
        }
    }


}
