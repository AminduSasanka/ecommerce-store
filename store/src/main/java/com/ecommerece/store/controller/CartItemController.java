package com.ecommerece.store.controller;

import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.User;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.cart.CartItemService;
import com.ecommerece.store.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        try {
            User user = userService.getAuthenticatedUser();

            cartItemService.addCartItem(user.getId(), productId, quantity);

            return ResponseEntity.ok().body(new ApiResponse("Item added to cart successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Resource not found", null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
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
