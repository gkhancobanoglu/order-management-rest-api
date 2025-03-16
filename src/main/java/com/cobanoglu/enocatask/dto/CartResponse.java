package com.cobanoglu.enocatask.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {

    private Long id;
    private BigDecimal totalPrice;
    private boolean isActive;
    private List<CartProductResponse> products;
}
