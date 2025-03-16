package com.cobanoglu.enocatask.repository;

import com.cobanoglu.enocatask.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
