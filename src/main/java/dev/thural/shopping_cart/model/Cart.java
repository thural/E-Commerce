package dev.thural.shopping_cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;
    private BigDecimal totalPrice;
}