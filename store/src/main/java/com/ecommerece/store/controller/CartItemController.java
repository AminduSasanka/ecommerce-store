package com.ecommerece.store.controller;

import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.CartItem;
import com.ecommerece.store.repository.CartItemRepository;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.cart.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("cartItems")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addToCart(@RequestParam Long cartId, @RequestParam Long productId,
                                                 @RequestParam int quantity) {
        try {
            cartItemService.addCartItem(cartId, productId, quantity);

            return ResponseEntity.ok().body(new ApiResponse("Item added to cart successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Resource not found", null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeCartItem(cartId, productId);

            return ResponseEntity.ok().body(new ApiResponse("Item deleted from cart successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart item not found", null));
        }
    }

    @GetMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId,
                                                          @RequestParam int quantity) {
        try {
            cartItemService.updateCartItem(cartId, productId, quantity);

            return ResponseEntity.ok().body(new ApiResponse("Item quantity updated", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart or cart item not found", null));
        }
    }

}
