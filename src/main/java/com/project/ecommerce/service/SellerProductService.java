package com.project.ecommerce.service;

import com.project.ecommerce.dto.ProductRequest;
import com.project.ecommerce.dto.ProductSummaryDTO;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.OrderItemRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.util.GenericSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SellerProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public void addProduct(Long sellerId, ProductRequest productRequest) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        try{
            Product product = new Product(productRequest.getName(),
                                          productRequest.getDescription(),
                                          productRequest.getPrice(),
                                          productRequest.getQuantity(),
                                          seller,
                                          productRequest.getImageUrl()
                                            );
            productRepository.save(product);
        }catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    public void updateProduct(Long sellerId, Long productId, ProductRequest productRequest) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!(product.getSeller().getId() == (seller.getId()))) {
            throw new RuntimeException("You are not authorized to update this product");
        }

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setImageUrl(productRequest.getImageUrl());

        productRepository.save(product);
    }

    public void deleteProduct(Long sellerId, Long productId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (!(product.getSeller().getId() == (seller.getId()))) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        productRepository.delete(product);
    }

    public List<ProductSummaryDTO> getProductsBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        List<Product> products = productRepository.findAllBySellerId(sellerId);
        return products.stream()
                .map(product -> new ProductSummaryDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl()))
                .toList();
    }

    public List<ProductSummaryDTO> searchProductsBySeller(Long sellerId, String query) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        List<Product> products = productRepository.findAllBySellerId(sellerId);

        GenericSearchUtil searchUtil = new GenericSearchUtil();
        return searchUtil.search(products, query, "name", "description")
                .stream()
                .map(product -> new ProductSummaryDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl()))
                .toList();
    }

    public List<ProductSummaryDTO> filterProductsBySeller(Long sellerId, String sortBy, String order) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        List<Product> products = productRepository.findAllBySellerId(sellerId);

        if (sortBy.equalsIgnoreCase("price")) {
            if (order.equalsIgnoreCase("asc")) {
                products.sort((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
            } else {
                products.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
            }
        } else if(sortBy.equalsIgnoreCase("buy-rate")){
                if (order.equalsIgnoreCase("asc")) {

                    products.sort((p1, p2) -> Long.compare(
                            orderItemRepository.countShippedItemsByProductId(p1.getId()),
                            orderItemRepository.countShippedItemsByProductId(p2.getId())
                    ));
                } else {
                    products.sort((p1, p2) -> Long.compare(
                            orderItemRepository.countShippedItemsByProductId(p2.getId()),
                            orderItemRepository.countShippedItemsByProductId(p1.getId())
                    ));
                }
        }
        else {
            throw new RuntimeException("Invalid sort parameters");
        }
        return products.stream()
                .map(product -> new ProductSummaryDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl()))
                .toList();
    }
}
