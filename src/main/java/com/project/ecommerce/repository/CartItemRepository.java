package com.project.ecommerce.repository;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByBuyer(User buyer);

    List<CartItem> findByBuyerId(Long buyerId);

    CartItem findByBuyerAndProduct(User buyer, Product product);

    void deleteByBuyerIdAndProductId(Long buyerId, Long productId);

    void deleteByBuyer(User buyer);

    void deleteByBuyerId(Long buyerId);

    Optional<CartItem> findByBuyerIdAndProductId(Long buyerId, Long productId);
}
