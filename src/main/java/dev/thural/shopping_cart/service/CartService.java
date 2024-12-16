package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.CartDto;
import jakarta.servlet.http.HttpSession;

public interface CartService {

    CartDto getCart(HttpSession session);

    void addItemToCart(CartDto cart, Product product, HttpSession session);

}