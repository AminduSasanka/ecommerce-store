package com.ecommerece.store.request;

import com.ecommerece.store.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private String brand;
    private Category category;
}
