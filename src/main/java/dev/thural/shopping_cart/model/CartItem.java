package dev.thural.shopping_cart.model;

import dev.thural.shopping_cart.entity.Product;
import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private Integer quantity;
}