package com.ecommerece.store.service.cart;

import com.ecommerece.store.dto.CartDto;
import com.ecommerece.store.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    CartDto getCartById(Long id);
    void clearCart(Long id);
    BigDecimal getCartTotal(Long id);
    CartDto getCartByUserId(Long id);

    CartDto convertToDto(Cart cart);

    Cart createOrGetExistingUserCart(Long userId);
}
