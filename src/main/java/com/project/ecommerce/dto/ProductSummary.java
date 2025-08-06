package com.project.ecommerce.dto;

import java.math.BigDecimal;

public class ProductSummary {

        private Long id;
        private String name;
        private BigDecimal price;
        private double averageRating;

    public ProductSummary() {}

    public ProductSummary(Long id, String name, BigDecimal price, double averageRating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.averageRating = averageRating;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
