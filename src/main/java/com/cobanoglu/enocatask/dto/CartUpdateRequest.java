package com.cobanoglu.enocatask.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartUpdateRequest {
    private List<CartProductUpdate> products;
}

