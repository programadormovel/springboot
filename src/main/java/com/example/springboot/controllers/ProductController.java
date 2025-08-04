package com.example.springboot.controllers;

import com.example.springboot.repositores.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    IProductRepository productRepository;

    
}
