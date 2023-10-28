package com.gamesi.worker.giftcard.controller;

import com.gamesi.worker.giftcard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping
    public ResponseEntity<?> findProduct(){
        return new ResponseEntity<>(productService.allProducts(), HttpStatus.OK);
    }

    @GetMapping("/{param}")
    public ResponseEntity<?> findAllProduct(@PathVariable String param){
        return new ResponseEntity<>(productService.findByParam(param), HttpStatus.OK);
    }
}
