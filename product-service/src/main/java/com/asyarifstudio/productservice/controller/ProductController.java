package com.asyarifstudio.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyarifstudio.productservice.dto.ProductResponse;
import com.asyarifstudio.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController{

    private final ProductService productService;

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable("id") String id){
        return productService.getProductById(id);
    }

}