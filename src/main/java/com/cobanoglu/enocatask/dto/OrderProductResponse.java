package com.cobanoglu.enocatask.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderProductResponse {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
