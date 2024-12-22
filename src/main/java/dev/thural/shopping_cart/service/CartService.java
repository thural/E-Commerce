package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.Cart;
import dev.thural.shopping_cart.entity.CartItem;
import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.CartDto;
import jakarta.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpSession session);

    CartDto getCartDto(HttpSession session);

    Cart addItemToCart(Cart cart, Product product);

    CartDto addItemToCartById(HttpSession session, Long productId);

    Cart removeItemFromCart(Cart cart, CartItem cartItem);

    CartDto removeItemFromCartById(HttpSession session, Long productId);
}