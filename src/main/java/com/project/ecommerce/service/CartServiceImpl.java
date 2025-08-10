package com.project.ecommerce.service;

import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.CartItemNotFoundException;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.repository.CartItemRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return cartItemRepository.findByBuyer(buyer);
    }

    @Override
    public List<CartItemResponse> getCartItemsResponse(Long userId) {
        return getCartItems(userId).stream()
                .map(cartItem -> new CartItemResponse(
                        cartItem.getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addToCart(Long userId, CartItemRequest cartItemRequest) {

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(cartItemRequest.getProductId()));

        // Check if product has enough stock
        if (product.getQuantity() < cartItemRequest.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getQuantity());
        }

        Optional<CartItem> existingCartItem = Optional.ofNullable(cartItemRepository.findByBuyerAndProduct(buyer, product));

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();

            // Check if new total quantity exceeds available stock
            if (product.getQuantity() < cartItemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + cartItemRequest.getQuantity());
            }

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem(buyer, product, cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
        }

        product.setQuantity(product.getQuantity() - cartItemRequest.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        CartItem cartItem = cartItemRepository.findByBuyerIdAndProductId(userId, productId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem not found for product ID: " + productId));

        cartItemRepository.delete(cartItem);
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        cartItemRepository.deleteByBuyer(buyer);
    }


}
