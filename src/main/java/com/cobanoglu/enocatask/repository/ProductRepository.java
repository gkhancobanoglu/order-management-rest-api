package com.cobanoglu.enocatask.repository;

import com.cobanoglu.enocatask.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
