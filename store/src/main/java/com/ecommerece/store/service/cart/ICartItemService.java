package com.ecommerece.store.service.cart;

import com.ecommerece.store.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartItemId, Long productId, int quantity);
    void removeCartItem(Long cartItemId, Long productId);
    void updateCartItem(Long cartItemId, Long productId, int quantity);

    CartItem getCartItemFromCart(Long cartId, Long productId);
}
