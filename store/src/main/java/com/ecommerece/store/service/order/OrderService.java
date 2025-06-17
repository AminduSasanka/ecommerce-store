package com.ecommerece.store.service.order;

import com.ecommerece.store.enums.OrderStatus;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Cart;
import com.ecommerece.store.model.Order;
import com.ecommerece.store.model.OrderItem;
import com.ecommerece.store.model.User;
import com.ecommerece.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

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

    @Override
    public Order placeOrder(User user, Cart cart) {
        Order order = createOrder(user);
        return null;
    }

    @Override
    public Order createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }
}
