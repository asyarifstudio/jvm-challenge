package com.asyarifstudio.reviewservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReviewResponse implements Serializable {
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
