package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.CartItem;

public interface CartItemService {

    CartItem saveCartItem(CartItem cartItem);

    CartItem getCartItemById(Long cartItemId);

    void deleteCartItem(CartItem cartItem);

}
