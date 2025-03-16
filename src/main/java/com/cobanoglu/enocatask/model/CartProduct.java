package com.cobanoglu.enocatask.model;

import com.cobanoglu.enocatask.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProduct extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;
}
