package com.asyarifstudio.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ProductResponse {
    String id;
    String name;
    Double averageReviewScore;
    Long numberOfRevierws;
}
