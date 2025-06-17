package com.ecommerece.store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal totalPrice;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product> product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
