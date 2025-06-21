package com.ecommerece.store.service.cart;

import com.ecommerece.store.dto.CartItemDto;
import com.ecommerece.store.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long userId, Long productId, int quantity);

    void removeCartItem(Long cartId, Long productId);
    void updateCartItem(Long cartItemId, Long productId, int quantity);

    CartItem getCartItemFromCart(Long cartId, Long productId);

    CartItemDto convertToDto(CartItem cartItem);
}
