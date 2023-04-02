package com.asyarifstudio.reviewservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.asyarifstudio.reviewservice.dto.ReviewRequest;
import com.asyarifstudio.reviewservice.dto.ReviewResponse;
import com.asyarifstudio.reviewservice.service.ReviewService;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponse> getAllProductReview(@PathVariable("productId") String productId){
        return reviewService.getAllProductReview(productId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody ReviewRequest reviewRequest){
        reviewService.addReview(reviewRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReview(@PathVariable("id") String id){
        reviewService.deleteReview(id);
    }

    @PutMapping("/{id}")
    public void updateReview(@PathVariable("id") String id, @RequestBody ReviewRequest reviewRequest){
        reviewService.updateReview(id, reviewRequest);
    }

}
