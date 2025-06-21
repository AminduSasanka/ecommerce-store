package com.ecommerece.store.service.cart;

import com.ecommerece.store.dto.CartDto;
import com.ecommerece.store.dto.CartItemDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Cart;
import com.ecommerece.store.repository.CartItemRepository;
import com.ecommerece.store.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final CartItemService cartItemService;

    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.setTotalAmount(cart.getTotalAmount());

        return convertToDto(cart);
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
    public CartDto getCartByUserId(Long id) {
        Cart cart = cartRepository.findByUserId(id);

        return convertToDto(cart);
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(cartItemService::convertToDto)
                .toList();

        cartDto.setCartItems(cartItemDtos);

        return cartDto;
    }
}
