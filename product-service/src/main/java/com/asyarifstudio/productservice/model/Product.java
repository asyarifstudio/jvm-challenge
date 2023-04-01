package com.asyarifstudio.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Product {
    String id;
    String name;
}
