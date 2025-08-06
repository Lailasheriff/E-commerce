package com.project.ecommerce.service;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;

import java.util.List;

public interface CartService {

    public List<CartItem> getCartItems(Long buyerId);

    public void addToCart(Long buyerId, Long productId, int quantity);

    public void removeFromCart(Long buyerId, Long productId);
}
