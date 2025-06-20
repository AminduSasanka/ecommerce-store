package com.ecommerece.store.service.order;

import com.ecommerece.store.dto.OrderDto;
import com.ecommerece.store.model.Order;
import com.ecommerece.store.model.OrderItem;

import java.util.List;

public interface IOrderService {
    Order getOrderById(Long orderId);

    List<OrderDto> getOrdersByUserId(Long userId);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    Order placeOrder(Long userId);
}
