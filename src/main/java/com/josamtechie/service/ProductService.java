package com.josamtechie.service;

import com.josamtechie.dao.ProductRepository;
import com.josamtechie.dto.ProductResponse;
import com.josamtechie.mapper.ProductResponseMapper;
import com.josamtechie.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> saveProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

//    public List<ProductResponse> getAllProducts(){
//        return productRepository.findAll()
//                .stream()
//                .map(ProductResponseMapper::map)
//                .collect(Collectors.toList());
//    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }


}
