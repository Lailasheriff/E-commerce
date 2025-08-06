package com.project.ecommerce.service;

import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
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
        System.out.println("Service: Getting cart items for user ID: " + userId);
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Service: Found user: " + buyer.getName());
        List<CartItem> cartItems = cartItemRepository.findByBuyer(buyer);
        System.out.println("Service: Found " + cartItems.size() + " cart items in database");
        return cartItems;
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
        System.out.println("Service: Adding to cart - User ID: " + userId + ", Product ID: " + cartItemRequest.getProductId() + ", Quantity: " + cartItemRequest.getQuantity());

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Service: Found user: " + buyer.getName() + " (ID: " + buyer.getId() + ")");

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        System.out.println("Service: Found product: " + product.getName() + " (ID: " + product.getId() + ")");

        // Check if product has enough stock
        if (product.getQuantity() < cartItemRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity());
        }

        Optional<CartItem> existingCartItem = Optional.ofNullable(cartItemRepository.findByBuyerAndProduct(buyer, product));

        if (existingCartItem.isPresent()) {
            System.out.println("Service: Updating existing cart item");
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();

            // Check if new total quantity exceeds available stock
            if (product.getQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + newQuantity);
            }

            cartItem.setQuantity(newQuantity);
            CartItem savedItem = cartItemRepository.save(cartItem);
            System.out.println("Service: Updated cart item with ID: " + savedItem.getId());
        } else {
            System.out.println("Service: Creating new cart item");
            CartItem cartItem = new CartItem(buyer, product, cartItemRequest.getQuantity());
            CartItem savedItem = cartItemRepository.save(cartItem);
            System.out.println("Service: Created new cart item with ID: " + savedItem.getId());
            System.out.println("Service: Cart item details - Buyer ID: " + savedItem.getBuyer().getId() + ", Product ID: " + savedItem.getProduct().getId() + ", Quantity: " + savedItem.getQuantity());
        }

        product.setQuantity(product.getQuantity() - cartItemRequest.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        System.out.println("Service: Removing from cart - User ID: " + userId + ", Product ID: " + productId);

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByBuyerAndProduct(buyer, product);
        if (cartItem == null) {
            throw new RuntimeException("Cart item not found");
        }

        cartItemRepository.delete(cartItem);
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        System.out.println("Service: Clearing cart for user ID: " + userId);

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItemRepository.deleteByBuyer(buyer);
        System.out.println("Service: Cleared all cart items for user");
    }

    @Override
    public int getCartItemCount(Long userId) {
        System.out.println("Service: Getting cart count for user ID: " + userId);

        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByBuyer(buyer);
        int count = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        System.out.println("Service: Cart count: " + count);
        return count;
    }
}
