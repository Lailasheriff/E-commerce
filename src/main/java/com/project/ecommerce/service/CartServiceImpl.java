package com.project.ecommerce.service;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.CartItemRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public List<CartItem> getCartItems(Long buyerId) {
        return cartItemRepository.findByBuyerId(buyerId);
    }

    @Override
    public void addToCart(Long buyerId, Long productId, int quantity) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingCartItem = cartItemRepository.findByBuyerIdAndProductId(buyerId, productId);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setBuyer(buyer);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeFromCart(Long buyerId, Long productId) {
        cartItemRepository.findByBuyerIdAndProductId(buyerId, productId);
    }
}
