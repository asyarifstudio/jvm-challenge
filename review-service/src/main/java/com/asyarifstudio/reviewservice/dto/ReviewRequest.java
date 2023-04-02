package com.asyarifstudio.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ReviewRequest {
    
      /**
     * reference to the productId
     */
    String productId;
    /**
     * The review Score, minimum 1 and maximum is 5
     */
    Integer reviewScore;

}
