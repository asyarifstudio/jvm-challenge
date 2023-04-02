package com.asyarifstudio.reviewservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Document("review")
public class Review {
    
    @Id
    String id;

    /**
     * reference to the productId
     */
    String productId;
    /**
     * The review Score, minimum 1 and maximum is 5
     */
    Integer reviewScore;
}
