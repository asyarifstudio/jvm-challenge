package com.asyarifstudio.reviewservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.asyarifstudio.reviewservice.dto.ReviewRequest;
import com.asyarifstudio.reviewservice.dto.ReviewResponse;
import com.asyarifstudio.reviewservice.model.Review;
import com.asyarifstudio.reviewservice.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    @CacheEvict(value = "reviews", key = "#reviewRequest.productId")
    public void addReview(ReviewRequest reviewRequest) {
        Review review = Review.builder()
                .productId(reviewRequest.getProductId())
                .reviewScore(reviewRequest.getReviewScore())
                .build();
        repository.save(review);

    }

    @Cacheable(value = "reviews", key = "#productId")
    public List<ReviewResponse> getAllProductReview(String productId) {
        List<Review> reviews = repository.findByProductId(productId);
        return reviews.stream().map(review -> ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .reviewScore(review.getReviewScore())
                .build()).collect(Collectors.toList());
    }

    @CacheEvict(value = "reviews", key = "#reviewRequest.productId")
    public boolean updateReview(String id, ReviewRequest reviewRequest) {
        Optional<Review> review = repository.findById(id);
        if (review!=null) {
            Review newReview = review.get();
            newReview.setProductId(reviewRequest.getProductId());
            newReview.setReviewScore(reviewRequest.getReviewScore());
            repository.save(newReview);
            return true;
        } else {
            return false;
        }
    }

    @CacheEvict(value = "reviews", allEntries = true/**
                                                     * delete cache for now, need to find a way to get the productId
                                                     * from id during cache eviction
                                                     */
    )
    public boolean deleteReview(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
