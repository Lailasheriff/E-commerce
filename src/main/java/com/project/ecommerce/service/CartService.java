package com.project.ecommerce.service;

import com.project.ecommerce.dto.CartItemRequest;
import com.project.ecommerce.dto.CartItemResponse;
import com.project.ecommerce.entity.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(Long userId);

    List<CartItemResponse> getCartItemsResponse(Long userId);

    void addToCart(Long userId, CartItemRequest cartItemRequest);

    void updateCartItem(Long userId, CartItemRequest cartItemRequest);

    void removeFromCart(Long userId, Long productId);

    void clearCart(Long userId);

    int getCartItemCount(Long userId);
}
