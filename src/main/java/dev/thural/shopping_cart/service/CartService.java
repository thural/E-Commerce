package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.request.CartRequest;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    CartDto getCartDto(HttpSession session);

    CartDto handleCartAction(HttpSession session, CartRequest request);

    CartDto incrementItemQuantity(HttpSession session, Long productId);

    CartDto decrementItemQuantity(HttpSession session, Long productId);

    CartDto removeCartItem(HttpSession session, Long itemId);
}