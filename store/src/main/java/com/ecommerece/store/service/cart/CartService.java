package com.ecommerece.store.service.cart;

import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Cart;
import com.ecommerece.store.model.CartItem;
import com.ecommerece.store.repository.CartItemRepository;
import com.ecommerece.store.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.setTotalAmount(cart.getTotalAmount());

        return cart;
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteAllByCartId(cart.getId());
        cart.getCartItems().clear();
        cartRepository.deleteById(cart.getId());
    }

    @Override
    public BigDecimal getCartTotal(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cart.getTotalAmount();
    }

    @Override
    public Cart getCartByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }
}
