package com.ecommerece.store.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    List<CartItemDto> cartItems = new ArrayList<>();
}
