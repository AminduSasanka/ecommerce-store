package com.ecommerece.store.controller;

import com.ecommerece.store.dto.OrderDto;
import com.ecommerece.store.dto.UserDto;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Order;
import com.ecommerece.store.response.ApiResponse;
import com.ecommerece.store.service.order.IOrderService;
import com.ecommerece.store.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> findAll() {
        return ResponseEntity.ok().body(new ApiResponse("Method not implemented yet", null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody Long userId) {
        try {
            UserDto user = userService.getUserById(userId);
            Order order = orderService.placeOrder(user.getId());

            return ResponseEntity.ok().body(new ApiResponse("Order placed", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);

            return ResponseEntity.ok().body(new ApiResponse("Order found", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Order not found", null));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> userOrders = orderService.getOrdersByUserId(userId);

            return ResponseEntity.ok().body(new ApiResponse("Orders found", userOrders));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error", e.getMessage()));
        }
    }
}
