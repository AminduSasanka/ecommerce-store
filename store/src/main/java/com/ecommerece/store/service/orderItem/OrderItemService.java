package com.ecommerece.store.service.orderItem;

import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.OrderItem;
import com.ecommerece.store.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderItemService implements IOrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItem getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));
    }
}
