package com.cobanoglu.enocatask.mapper;

import com.cobanoglu.enocatask.dto.*;
import com.cobanoglu.enocatask.model.Order;
import com.cobanoglu.enocatask.model.OrderProduct;
import com.cobanoglu.enocatask.model.Product;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .totalPrice(order.getTotalPrice())
                .orderProducts(order.getOrderProducts().stream()
                        .map(OrderMapper::toOrderProductResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public static OrderProductResponse toOrderProductResponse(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return OrderProductResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice())
                .build();
    }
}
