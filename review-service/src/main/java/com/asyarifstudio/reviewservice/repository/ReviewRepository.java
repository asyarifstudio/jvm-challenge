package com.asyarifstudio.reviewservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.asyarifstudio.reviewservice.model.Review;

public interface ReviewRepository extends MongoRepository<Review,String> {
    List<Review> findByProductId(String productId);
}
