package com.project.ecommerce.controller;

import com.project.ecommerce.dto.AuthResponse;
import com.project.ecommerce.dto.LoginRequest;
import com.project.ecommerce.dto.SignupRequest;
import com.project.ecommerce.entity.Role;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {

        // get fields from request body
        String name = signupRequest.getName();
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        Role role = signupRequest.getRole();

        // check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already Exists!");
        }

        // get hashed password
        String encodedPassword = passwordEncoder.encode(password);

        // create and save user
        User user = new User(name, email, encodedPassword, role);
        userRepository.save(user);

        // return success response
        return ResponseEntity.ok("User Registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        // looks for the user by email
        Optional<User> potentialUser = userRepository.findByEmail(loginRequest.getEmail());

        // returns 401 unauthorized if not found
        if (potentialUser.isEmpty()) {
            return  ResponseEntity.status(401).body("Invalid email or password!");
        }

        User user = potentialUser.get();
        // compares entered password with stored hashed password
        // returns 401 unauthorized if it doesn't match
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return  ResponseEntity.status(401).body("Invalid password!");
        }

        String token = "token";

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
