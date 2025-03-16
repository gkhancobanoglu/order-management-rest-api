package com.cobanoglu.enocatask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Order products cannot be null")
    private List<OrderProductRequest> orderProducts;
}
