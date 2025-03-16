package com.cobanoglu.enocatask.controller;

import com.cobanoglu.enocatask.dto.*;
import com.cobanoglu.enocatask.model.Cart;
import com.cobanoglu.enocatask.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @PutMapping("/{cartId}/update")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long cartId,
                                                   @RequestBody CartUpdateRequest updateRequest) {
        return ResponseEntity.ok(cartService.updateCart(cartId, updateRequest));
    }


    @PostMapping("/{cartId}/add-product/{productId}")
    public ResponseEntity<CartResponse> addProductToCart(@PathVariable Long cartId,
                                                         @PathVariable Long productId,
                                                         @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(cartId, productId, quantity));
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody CartCreate cartCreate) {
        CartResponse createdCart = cartService.createCart(cartCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<OrderResponse> checkoutCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.checkoutCart(cartId));
    }



    @DeleteMapping("/{cartId}/remove-product/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(cartId, productId));
    }

    @DeleteMapping("/{id}/empty")
    public ResponseEntity<CartResponse> emptyCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.emptyCart(id));
    }
}
