package com.ecommerece.store.controller;

import com.ecommerece.store.dto.CartDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCarts(@PathVariable Long id) {
        try {
            CartDto cart = cartService.getCartById(id);

            return ResponseEntity.ok().body(new ApiResponse("Cart found!", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }

    @DeleteMapping("{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartService.clearCart(id);

            return ResponseEntity.ok().body(new ApiResponse("Cart cleared!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id) {
        try {
            BigDecimal totalAmount = cartService.getCartTotal(id);

            return ResponseEntity.ok().body(new ApiResponse("Cart total amount", totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
        }
    }
}
