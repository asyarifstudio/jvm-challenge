package com.asyarifstudio.productservice.service;

import org.springframework.stereotype.Service;

import com.asyarifstudio.productservice.dto.ProductResponse;

@Service
public class ProductService {
    
    public ProductResponse getProductById(String id){
        return ProductResponse.builder().id(id).name("test").build();
    }
}
