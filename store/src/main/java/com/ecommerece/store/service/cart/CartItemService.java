package com.ecommerece.store.service.cart;

import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Cart;
import com.ecommerece.store.model.CartItem;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.repository.CartItemRepository;
import com.ecommerece.store.repository.CartRepository;
import com.ecommerece.store.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ICartService cartService;
    private final IProductService productService;

    @Override
    public void addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);

        cart.addItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartItemId, Long productId) {
        Cart cart = cartService.getCartById(cartItemId);
        CartItem itemToRemove = getCartItemFromCart(cart.getId(), productId);

        cart.removeItem(itemToRemove);
        cartRepository.save(cart);  // triggers orphan removal and saves new total
    }

    @Override
    public void updateCartItem(Long cartItemId, Long productId, int quantity) {
        Cart cart = cartService.getCartById(cartItemId);

        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        BigDecimal totalAmount = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItemFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    }
}
