package com.cobanoglu.enocatask.service.impl;

import com.cobanoglu.enocatask.dto.*;
import com.cobanoglu.enocatask.exception.CartNotFoundException;
import com.cobanoglu.enocatask.exception.CustomerNotFoundException;
import com.cobanoglu.enocatask.exception.ProductNotFoundException;
import com.cobanoglu.enocatask.mapper.CartMapper;
import com.cobanoglu.enocatask.mapper.OrderMapper;
import com.cobanoglu.enocatask.model.*;
import com.cobanoglu.enocatask.repository.*;
import com.cobanoglu.enocatask.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public CartResponse getCart(Long id) {
        Cart cart = getCartEntity(id);
        return CartMapper.toCartResponse(cart);
    }

    @Override
    public List<CartResponse> getAllCarts() {
        return cartRepository.findAll().stream()
                .map(CartMapper::toCartResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse updateCart(Long cartId, CartUpdateRequest updateRequest) {
        Cart cart = getCartEntity(cartId);

        BigDecimal newTotalPrice = BigDecimal.ZERO;

        for (CartProductUpdate productUpdate : updateRequest.getProducts()) {
            Product product = productRepository.findById(productUpdate.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productUpdate.getProductId()));

            // Stok kontrolü
            if (product.getStock() < productUpdate.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Sepetteki ürünü bul veya oluştur
            CartProduct cartProduct = cart.getCartProducts().stream()
                    .filter(cp -> cp.getProduct().getId().equals(productUpdate.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartProduct != null) {
                // Miktar sıfırsa ürünü sepetten çıkar
                if (productUpdate.getQuantity() == 0) {
                    cart.getCartProducts().remove(cartProduct);
                    cartProductRepository.delete(cartProduct);
                } else {
                    // Miktarı güncelle
                    cartProduct.setQuantity(productUpdate.getQuantity());
                    cartProductRepository.save(cartProduct);
                    newTotalPrice = newTotalPrice.add(cartProduct.getPrice().multiply(BigDecimal.valueOf(productUpdate.getQuantity())));
                }
            } else if (productUpdate.getQuantity() > 0) {
                // Yeni ürün ekleniyorsa
                CartProduct newCartProduct = CartProduct.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(productUpdate.getQuantity())
                        .price(product.getPrice())
                        .build();
                cart.getCartProducts().add(newCartProduct);
                cartProductRepository.save(newCartProduct);

                newTotalPrice = newTotalPrice.add(newCartProduct.getPrice().multiply(BigDecimal.valueOf(productUpdate.getQuantity())));
            }
        }

        // Toplam fiyatı güncelle
        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);

        return CartMapper.toCartResponse(cart);
    }


    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartEntity(id);
        cartRepository.delete(cart);
    }

    @Override
    public CartResponse addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = getCartEntity(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        // Stok kontrolü
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product with id: " + productId);
        }

        CartProduct cartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> CartProduct.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(0)
                        .price(product.getPrice())
                        .build());

        cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        cartProductRepository.save(cartProduct);

        if (!cart.getCartProducts().contains(cartProduct)) {
            cart.getCartProducts().add(cartProduct);
        }

        updateCartTotalPrice(cart);
        return CartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse removeProductFromCart(Long cartId, Long productId) {
        Cart cart = getCartEntity(cartId);
        cart.getCartProducts().removeIf(cp -> cp.getProduct().getId().equals(productId));
        updateCartTotalPrice(cart);
        return CartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse emptyCart(Long id) {
        Cart cart = getCartEntity(id);
        cart.getCartProducts().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        return CartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse createCart(CartCreate cartCreate) {
        Customer customer = customerRepository.findById(cartCreate.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + cartCreate.getCustomerId()));

        Cart cart = Cart.builder()
                .customer(customer)
                .totalPrice(BigDecimal.ZERO)
                .isActive(true)
                .cartProducts(new ArrayList<>())
                .build();

        return CartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public OrderResponse checkoutCart(Long cartId) {
        Cart cart = getCartEntity(cartId);

        // Sepet boşsa hata ver
        if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty and cannot be checked out.");
        }

        // Stok kontrolü
        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = cartProduct.getProduct();
            if (product.getStock() < cartProduct.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }

        // Stokları güncelle
        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = cartProduct.getProduct();
            product.setStock(product.getStock() - cartProduct.getQuantity());
            productRepository.save(product);
        }

        // Siparişi oluştur
        Order order = Order.builder()
                .customer(cart.getCustomer())
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.PENDING)
                .orderProducts(new ArrayList<>()) // Listeyi boş olarak başlatalım
                .isActive(true)
                .build();

        order = orderRepository.save(order); // Önce Order'u kaydet

        // Sipariş ürünlerini oluştur ve kaydet
        for (CartProduct cartProduct : cart.getCartProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProduct.setPrice(cartProduct.getPrice());
            order.getOrderProducts().add(orderProduct);
        }

        order = orderRepository.save(order); // OrderProduct'larla birlikte tekrar kaydet

        // Sepeti boşalt
        cart.getCartProducts().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return OrderMapper.toOrderResponse(order);
    }




    private Cart getCartEntity(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));
    }

    private void updateCartTotalPrice(Cart cart) {
        BigDecimal total = cart.getCartProducts().stream()
                .map(cp -> cp.getPrice().multiply(BigDecimal.valueOf(cp.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
}
