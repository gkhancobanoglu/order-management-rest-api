package com.cobanoglu.enocatask.service;

import com.cobanoglu.enocatask.dto.ProductRequest;
import com.cobanoglu.enocatask.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    void deleteProduct(Long id);
}
