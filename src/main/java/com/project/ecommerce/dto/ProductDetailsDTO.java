package com.project.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantityAvailable;
    private double averageRating;
    private int totalReviews;
    private List<ReviewDTO> reviewDTOS;
    private String imageUrl;


    public ProductDetailsDTO() {}

    public ProductDetailsDTO(Long id, String name, BigDecimal price, double averageRating, int quantityAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.averageRating = averageRating;
        this.quantityAvailable = quantityAvailable;
    }

    public ProductDetailsDTO(Long id, String name, String description, BigDecimal price, int quantityAvailable, double averageRating, int totalReviews, List<ReviewDTO> reviewDTOS, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.reviewDTOS = reviewDTOS;
        this.imageUrl = imageUrl;
    }



    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public List<ReviewDTO> getReviews() {
        return reviewDTOS;
    }

    public void setReviews(List<ReviewDTO> reviewDTOS) {
        this.reviewDTOS = reviewDTOS;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
