package com.project.ecommerce.service;

import com.project.ecommerce.entity.*;
import com.project.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private CartItemRepository cartItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository
    , ProductRepository productRepository, OrderItemRepository orderItemRepository
    , CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public void checkout(Long buyerId) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow( () -> new RuntimeException("Buyer not found"));

        List<CartItem> cartItems = cartItemRepository.findByBuyerId(buyerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("No cartItems found");
        }
        Order order = new Order();
        order.setBuyer(buyer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            totalPrice = totalPrice.add(product.getPrice().multiply((BigDecimal.valueOf(cartItem.getQuantity()))));
            orderItemRepository.save(orderItem);
        }

        order.setTotal(totalPrice);
        orderRepository.save(order);

        cartItemRepository.deleteByBuyerId(buyerId);
    }

    @Override
    public List<Order> getOrderHistory(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
}
