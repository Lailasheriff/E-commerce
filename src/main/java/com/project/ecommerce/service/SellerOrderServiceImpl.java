package com.project.ecommerce.service;
import com.project.ecommerce.dto.OrderResponse;
import com.project.ecommerce.dto.OrderItemResponse;
import com.project.ecommerce.dto.ProductDetailsDTO;
import com.project.ecommerce.dto.ReviewDTO;
import com.project.ecommerce.entity.*;
import com.project.ecommerce.exception.InvalidOrderStatusException;
import com.project.ecommerce.exception.OrderNotFoundException;
import com.project.ecommerce.exception.ProductNotFoundException;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



@Service
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public ProductDetailsDTO getProductDetailsById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setQuantityAvailable(product.getQuantity());
        dto.setAverageRating(product.getAverageRating());
        dto.setTotalReviews(product.getTotalReviews());

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : product.getReviews()) {
            ReviewDTO reviewDTO = new ReviewDTO(
                    review.getCreatedAt(),
                    review.getComment(),
                    review.getRating(),
                    review.getUser().getName(),
                    review.getId()
            );

            reviewDTOList.add(reviewDTO);
        }

        dto.setReviews(reviewDTOList);


        Integer totalSold = productRepository.getTotalQuantitySoldByProductId(productId);
        Integer totalOrders = productRepository.getTotalOrdersByProductId(productId);
        dto.setQuantitySold(totalSold != null ? totalSold : 0);
        dto.setTotalOrders(totalOrders != null ? totalOrders : 0);

        return dto;
    }


    @Override
    public List<OrderResponse> getOrdersBySellerId(Long sellerId) {
        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse response = new OrderResponse();
            response.setOrderId(order.getId());
            response.setOrderDate(order.getCreatedAt());
            response.setOrderStatus(order.getStatus());
            response.setBuyerName(order.getBuyer().getName());

            List<OrderItemResponse> itemResponses = new ArrayList<>();
            order.getItems().forEach(item -> {
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setProductId(item.getProduct().getId());
                itemResponse.setProductName(item.getProduct().getName());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPrice(item.getProduct().getPrice());

                itemResponses.add(itemResponse);
            });

            response.setOrderItems(itemResponses);
            responseList.add(response);
        }

        return responseList;
    }



    @Override
    public String updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            orderRepository.save(order);

            return "Order status updated to " + newStatus;
        }

        catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("Invalid Order Status");
        }
    }



    @Override
    public List<ProductDetailsDTO> getProductsStatsSorted(Long sellerId, String sortBy) {
        List<Object[]> stats = productRepository.getProductSalesStats(sellerId);
        List<ProductDetailsDTO> list = new ArrayList<>();

        for (Object[] row : stats) {
            Long productId = (Long) row[0];
            String name = (String) row[1];
            Long quantitySold = (Long) row[2];

            ProductDetailsDTO dto = new ProductDetailsDTO();
            dto.setId(productId);
            dto.setName(name);
            dto.setQuantitySold(quantitySold.intValue());

            list.add(dto);
        }

        if ("asc".equalsIgnoreCase(sortBy)) {
            list.sort(Comparator.comparingInt(ProductDetailsDTO::getQuantitySold));
        }

        else {
            list.sort(Comparator.comparingInt(ProductDetailsDTO::getQuantitySold).reversed());
        }

        return list;
    }

}