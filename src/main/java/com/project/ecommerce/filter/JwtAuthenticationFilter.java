package com.project.ecommerce.filter;

import com.project.ecommerce.entity.Role;
import com.project.ecommerce.exception.AccessDeniedForBuyerException;
import com.project.ecommerce.exception.AccessDeniedForSellerException;
import com.project.ecommerce.exception.InvalidJwtTokenException;
import com.project.ecommerce.exception.MissingAuthorizationHeaderException;
import com.project.ecommerce.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        // Skip authentication for /auth endpoints
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new MissingAuthorizationHeaderException();
            }

            String token = authHeader.substring(7); // Remove "Bearer "

            if (!jwtService.isTokenValid(token)) {
                throw new InvalidJwtTokenException();
            }

            Long userId = jwtService.extractId(token);
            Role role = jwtService.extractRole(token);

            if(path.startsWith("/seller") && role != Role.SELLER) {
                throw new AccessDeniedForSellerException();
            }

            if(path.startsWith("/buyer") && role != Role.BUYER) {
                throw new AccessDeniedForBuyerException();
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("JWT Filter Error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
