package com.project.ecommerce.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private String reviewerName;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public ReviewDTO() {}

    public ReviewDTO(Timestamp createdAt, String comment, int rating, String reviewerName, Long id) {
        this.createdAt = createdAt;
        this.comment = comment;
        this.rating = rating;
        this.reviewerName = reviewerName;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
