package com.project.ecommerce.dto;

import java.math.BigDecimal;

public class ProductSummaryDTO {


        private String name;
        private BigDecimal price;
        private String imageUrl;

    public ProductSummaryDTO() {}

    public ProductSummaryDTO(String name, BigDecimal price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
