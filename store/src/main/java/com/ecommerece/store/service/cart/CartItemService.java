package com.ecommerece.store.service.cart;

import com.ecommerece.store.dto.CartItemDto;
import com.ecommerece.store.dto.ProductDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Cart;
import com.ecommerece.store.model.CartItem;
import com.ecommerece.store.model.Product;
import com.ecommerece.store.repository.CartItemRepository;
import com.ecommerece.store.repository.CartRepository;
import com.ecommerece.store.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public void addCartItem(Long userId, Long productId, int quantity) throws ResourceNotFoundException {
        Cart cart = createOrGetExistingUserCart(userId);
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
    public void removeCartItem(Long cartId, Long productId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        CartItem itemToRemove = getCartItemFromCart(cart.getId(), productId);

        cart.removeItem(itemToRemove);
        cartRepository.save(cart);  // triggers orphan removal and saves new total
    }

    @Override
    public void updateCartItem(Long cartItemId, Long productId, int quantity) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

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
    public CartItem getCartItemFromCart(Long cartId, Long productId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    }

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
        ProductDto productDto = productService.convertToDto(cartItem.getProduct());

        cartItemDto.setProduct(productDto);

        return cartItemDto;
    }

    private Cart createOrGetExistingUserCart(Long userId) {
        Cart userCart = cartRepository.findByUserId(userId);

        if (userCart.getId() == null) {
            userCart = new Cart();
            userCart.setId(userId);
            cartRepository.save(userCart);
        }

        return userCart;
    }
}
