package com.cobanoglu.enocatask.service;

import com.cobanoglu.enocatask.dto.OrderRequest;
import com.cobanoglu.enocatask.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderResponse getOrder(Long id);

    List<OrderResponse> getAllOrdersForCustomer(Long customerId);

    void deleteOrder(Long id);

    OrderResponse placeOrderFromCart(Long cartId);

    OrderResponse getOrderByCode(Long orderCode);
}
