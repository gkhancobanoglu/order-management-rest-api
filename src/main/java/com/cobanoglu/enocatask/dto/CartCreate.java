package com.cobanoglu.enocatask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartCreate {
    @NotNull
    private Long customerId;
}
