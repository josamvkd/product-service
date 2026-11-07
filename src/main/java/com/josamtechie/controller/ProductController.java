package com.josamtechie.controller;

import com.josamtechie.dto.ProductResponse;
import com.josamtechie.model.Product;
import com.josamtechie.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService)
    {
        this.productService = productService;
    }

    @PostMapping
    public List<Product> addProducts(@RequestBody List<Product> products) {
        return productService.saveProducts(products);
    }

//    @GetMapping
//    public List<ProductResponse> getAllProducts() {
//        return productService.getAllProducts();
//    }

    @GetMapping
    public List<Product> getAllProducts()
    {
        return productService.getAllProducts();
    }


}
