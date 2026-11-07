package com.josamtechie.mapper;

import com.josamtechie.dto.ProductResponse;
import com.josamtechie.model.Product;

public class ProductResponseMapper
{

    public static ProductResponse map(Product product)
    {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .discountPercentage(product.getDiscountPercentage())
                .priceAfterDiscount(product.getPrice() - (product.getPrice() * product.getDiscountPercentage() / 100))
                .build();
    }
}
