package com.ecommerece.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;

    private void updateTotalAmount() {
        this.totalAmount = cartItems.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();

            if (unitPrice == null) {
                return BigDecimal.ZERO;
            }

            return unitPrice.multiply(new BigDecimal(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        this.updateTotalAmount();
    }

    public void removeItem(CartItem itemToRemove) {
        this.cartItems.remove(itemToRemove);  // removes from collection
        itemToRemove.setCart(null);           // breaks bidirectional link
        this.updateTotalAmount();             // updates cart total
    }
}
