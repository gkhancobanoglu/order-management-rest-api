package com.cobanoglu.enocatask.repository;

import com.cobanoglu.enocatask.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long customerId);

    Optional<Order> findByOrderCode(Long orderCode);

}
