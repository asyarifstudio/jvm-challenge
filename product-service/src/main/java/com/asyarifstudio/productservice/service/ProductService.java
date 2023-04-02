package com.asyarifstudio.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.asyarifstudio.productservice.dto.ProductResponse;
import com.asyarifstudio.productservice.dto.ReviewResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final WebClient webClient;
    
    public ProductResponse getProductById(String id){
        
        ProductResponse productResponse =  ProductResponse.builder().id(id).name("test").build();

        //fetch the review to the review service
        ReviewResponse[] reviews = webClient.get()
            .uri("http://localhost:8081/api/review/"+id)
            .retrieve()
            .bodyToMono(ReviewResponse[].class)
            .block();

        //compute the review
        Long reviewScores = 0L;
        Long numberOfReviews = 0L;
        for(ReviewResponse review:reviews){
            reviewScores += review.getReviewScore();
            numberOfReviews += 1;
        }

        productResponse.setAverageReviewScore(reviewScores.doubleValue()/numberOfReviews);
        productResponse.setNumberOfRevierws(numberOfReviews);

        return productResponse;
    }
}
