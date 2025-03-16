package com.cobanoglu.enocatask.mapper;

import com.cobanoglu.enocatask.dto.CartProductResponse;
import com.cobanoglu.enocatask.dto.CartResponse;
import com.cobanoglu.enocatask.model.Cart;
import com.cobanoglu.enocatask.model.CartProduct;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .isActive(cart.isActive())
                .products(cart.getCartProducts().stream()
                        .map(CartMapper::toCartProductResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public static CartProductResponse toCartProductResponse(CartProduct cartProduct) {
        return CartProductResponse.builder()
                .productId(cartProduct.getProduct().getId())
                .productName(cartProduct.getProduct().getName())
                .quantity(cartProduct.getQuantity())
                .price(cartProduct.getPrice())
                .build();
    }
}
