package com.asyarifstudio.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReviewResponse {
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
