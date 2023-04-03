package com.asyarifstudio.reviewservice.service;

import java.util.List;
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

    public void addReview(ReviewRequest reviewRequest){
        Review review = Review.builder()
                        .productId(reviewRequest.getProductId())
                        .reviewScore(reviewRequest.getReviewScore())
                        .build();
        repository.save(review);

    }

    @Cacheable(value = "reviews")
    public List<ReviewResponse> getAllProductReview(String productId){
        List<Review> reviews = repository.findByProductId(productId);
        return reviews.stream().map(review ->
            ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .reviewScore(review.getReviewScore())
                .build()
        ).collect(Collectors.toList());
    }

    @CacheEvict(value = "reviews",allEntries = true)
    public boolean updateReview(String id,ReviewRequest reviewRequest){
        if(repository.existsById(id)){
            Review review = Review.builder()
                        .id(id)
                        .productId(reviewRequest.getProductId())
                        .reviewScore(reviewRequest.getReviewScore())
                        .build();
             repository.save(review);
             return true;
        }else{
            return false;
        }
    }
    @CacheEvict(value = "reviews",allEntries = true)
    public boolean deleteReview(String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        else{
            return false;
        }
    }
}
