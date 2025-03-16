package com.cobanoglu.enocatask.model;

import com.cobanoglu.enocatask.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 255)
    @NotBlank
    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price =  BigDecimal.ZERO;

    @Column(nullable = false)
    @Min(0)
    private int stock;

    @Column(nullable = false)
    private boolean isActive = true;


}
