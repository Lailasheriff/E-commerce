package com.project.ecommerce.service;

import com.project.ecommerce.dto.ReviewRequest;

public interface ReviewService {

    void submitReview(Long buyerId, ReviewRequest reviewRequest);
}
