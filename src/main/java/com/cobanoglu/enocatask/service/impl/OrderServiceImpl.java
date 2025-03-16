package com.cobanoglu.enocatask.service.impl;

import com.cobanoglu.enocatask.dto.OrderRequest;
import com.cobanoglu.enocatask.dto.OrderResponse;
import com.cobanoglu.enocatask.exception.CartNotFoundException;
import com.cobanoglu.enocatask.exception.OrderNotFoundException;
import com.cobanoglu.enocatask.exception.ProductNotFoundException;
import com.cobanoglu.enocatask.mapper.OrderMapper;
import com.cobanoglu.enocatask.model.*;
import com.cobanoglu.enocatask.repository.*;
import com.cobanoglu.enocatask.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setOrderCode(System.currentTimeMillis());
        order.setCustomer(customer);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderProduct> orderProducts = request.getOrderProducts().stream().map(op -> {
            Product product = productRepository.findById(op.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + op.getProductId()));

            if (product.getStock() < op.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - op.getQuantity());
            totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(op.getQuantity())));

            return OrderProduct.builder()
                    .order(order)
                    .product(product)
                    .quantity(op.getQuantity())
                    .price(product.getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<OrderResponse> getAllOrdersForCustomer(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId).stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponse placeOrderFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        if (cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot place an order.");
        }

        // Order oluştur
        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderCode(System.currentTimeMillis());
        order.setTotalPrice(cart.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());

        // Order Products ekle
        List<OrderProduct> orderProducts = cart.getCartProducts().stream()
                .map(cp -> OrderProduct.builder()
                        .order(order)
                        .product(cp.getProduct())
                        .quantity(cp.getQuantity())
                        .price(cp.getPrice())
                        .build())
                .toList();
        order.setOrderProducts(orderProducts);

        // Order kaydet
        Order savedOrder = orderRepository.save(order);

        // Sepeti boşalt
        cart.getCartProducts().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return OrderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderByCode(Long orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with code: " + orderCode));
        return OrderMapper.toOrderResponse(order);
    }
}
