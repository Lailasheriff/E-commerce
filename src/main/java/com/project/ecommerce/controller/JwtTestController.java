package com.project.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/jwt-test")
public class JwtTestController {
    @Autowired
    private JwtService jwtService;

    // 1. Generate token from username/email
    @GetMapping("/generate")
    public Map<String, String> generateToken(@RequestParam String username) {
        String token = jwtService.generateToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // 2. Extract username from token
    @GetMapping("/extract")
    public Map<String, String> extractUsername(@RequestParam String token) {
        String username = jwtService.extractEmail(token);
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        return response;
    }

    // 3. Validate token against username
    @GetMapping("/validate")
    public Map<String, Object> validateToken(@RequestParam String token) {
        boolean valid = jwtService.isTokenValid(token);
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", valid);
        return response;
    }
}
