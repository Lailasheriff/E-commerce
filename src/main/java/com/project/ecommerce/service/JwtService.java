package com.project.ecommerce.service;


import com.project.ecommerce.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Long id, Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        String subject = extractClaim(token, Claims::getSubject); // "sub" is stored as String
        return Long.parseLong(subject); // Convert to Long
    }

    public Role extractRole(String token) {
        String roleStr = extractClaim(token, claims -> claims.get("role", String.class));
        return Role.valueOf(roleStr); // Convert string back to enum
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        final Role extractedRole= extractRole(token);
        final Long extractedId= extractId(token);
        boolean expired = isTokenExpired(token);



        System.out.println("Extracted Role: " + extractedRole);
        System.out.println("Extracted Id: " + extractedId);

        System.out.println("Token expired: " + expired);


        boolean isValid =!expired;
        System.out.println("Final isValid: " + isValid);
        return isValid;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long extractUserIdFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // Validate the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        // Extract the token from the header and remove "Bearer " prefix
        String token = authHeader.substring(7);

        // Validate the JWT token
        if (isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }

        return extractId(token);
    }
}
