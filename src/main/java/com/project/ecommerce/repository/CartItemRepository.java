package com.project.ecommerce.repository;

import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByBuyer(User user);

    CartItem findByBuyerAndProduct(User buyer, Product product);

    void deleteByBuyerAndProduct(User buyer, Product product);

    void deleteByBuyer(User buyer);
}
