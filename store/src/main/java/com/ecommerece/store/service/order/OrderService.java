package com.ecommerece.store.service.order;

import com.ecommerece.store.enums.OrderStatus;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.*;
import com.ecommerece.store.repository.OrderRepository;
import com.ecommerece.store.repository.ProductRepository;
import com.ecommerece.store.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    public Order getOrderById(Long orderId) throws ResourceNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByUserId(User user) {
        return orderRepository.findAllByUser(user);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) throws ResourceNotFoundException {
        return getOrderById(orderId).getOrderItems();
    }

    @Transactional
    @Override
    public Order placeOrder(User user) {
        Cart cart = cartService.getCartByUserId(user.getId());

        Order order = createOrder(user);
        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setOrderItems(orderItems);
        order.setTotalAmount(calculateTotalAmount(order));

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return order;
    }

    private Order createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            productRepository.save(product);

            OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity());
            orderItem.setTotalPrice();

            return orderItem;
        }).toList();
    }

    private BigDecimal calculateTotalAmount(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getOrderItems()) {
            total = total.add(orderItem.getTotalPrice());
        }

        return total;
    }
}
