package com.cobanoglu.enocatask.mapper;

import com.cobanoglu.enocatask.dto.ProductRequest;
import com.cobanoglu.enocatask.dto.ProductResponse;
import com.cobanoglu.enocatask.model.Product;

public class ProductMapper {

    public static Product toProduct(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .isActive(request.isActive())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .isActive(product.isActive())
                .build();
    }
}
