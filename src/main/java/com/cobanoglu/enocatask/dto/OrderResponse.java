package com.cobanoglu.enocatask.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long orderCode;
    private BigDecimal totalPrice;
    private List<OrderProductResponse> orderProducts;
}
