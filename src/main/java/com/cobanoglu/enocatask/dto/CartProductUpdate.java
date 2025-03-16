package com.cobanoglu.enocatask.dto;

import lombok.Data;

@Data
public class CartProductUpdate {
    private Long productId;
    private int quantity;
}