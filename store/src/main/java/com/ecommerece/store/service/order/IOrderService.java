package com.ecommerece.store.service.order;

import com.ecommerece.store.model.Cart;
import com.ecommerece.store.model.Order;
import com.ecommerece.store.model.OrderItem;
import com.ecommerece.store.model.User;

import java.util.List;

public interface IOrderService {
    Order getOrderById(Long orderId);

    List<Order> getOrdersByUserId(User user);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    Order placeOrder(User user, Cart cart);

    Order createOrder(User user);
}
