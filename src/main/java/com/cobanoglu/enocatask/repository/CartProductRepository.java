package com.cobanoglu.enocatask.repository;

import com.cobanoglu.enocatask.model.CartProduct;
import org.springframework.data.repository.CrudRepository;

public interface CartProductRepository extends CrudRepository<CartProduct, Long> {
}
