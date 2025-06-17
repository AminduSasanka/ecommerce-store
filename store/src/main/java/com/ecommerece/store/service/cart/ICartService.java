package com.ecommerece.store.service.cart;

import com.ecommerece.store.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCartById(Long id);
    void clearCart(Long id);
    BigDecimal getCartTotal(Long id);
    Cart getCartByUserId(Long id);
}
