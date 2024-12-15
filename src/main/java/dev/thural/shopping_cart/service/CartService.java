package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.Cart;
import jakarta.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpSession session);

    void addItemToCart(Cart cart, Product product, HttpSession session);

}