package com.ecommerece.store.repository;

import com.ecommerece.store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
