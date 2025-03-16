package com.cobanoglu.enocatask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Product price cannot be null")
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must be zero or more")
    private int stock;

    private boolean isActive = true;
}
