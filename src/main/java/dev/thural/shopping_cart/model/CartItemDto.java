package dev.thural.shopping_cart.model;

import dev.thural.shopping_cart.entity.Product;
import lombok.Data;

@Data
public class CartItemDto {
    private Product product;
    private Integer quantity;
}