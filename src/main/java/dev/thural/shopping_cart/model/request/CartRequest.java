package dev.thural.shopping_cart.model.request;

import dev.thural.shopping_cart.emums.CartAction;
import lombok.Data;

@Data
public class CartRequest {
    private Long itemId;
    private CartAction action;
}
