package com.cobanoglu.enocatask.service;

import com.cobanoglu.enocatask.dto.*;
import com.cobanoglu.enocatask.model.Cart;

import java.util.List;

public interface CartService {

    CartResponse getCart(Long id);

    List<CartResponse> getAllCarts();

    CartResponse updateCart(Long id, CartUpdateRequest updateRequest);

    void deleteCart(Long id);

    CartResponse addProductToCart(Long cartId, Long productId, int quantity);

    CartResponse removeProductFromCart(Long cartId, Long productId);

    CartResponse emptyCart(Long id);

    CartResponse createCart(CartCreate cartCreate);

    OrderResponse checkoutCart(Long cartId);
}
