package com.example.springboot.controllers;

import com.example.springboot.repositores.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    IProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(
            @RequestBody @Valid ProductRecordDto productRecordDto
            ){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(
                productRecordDto, productModel
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRepository.save(productModel));
    }



}
