package com.ecommerece.store.repository;

import com.ecommerece.store.model.Order;
import com.ecommerece.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByUserId(Long userId);
}
