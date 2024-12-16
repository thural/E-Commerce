package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.CartItemDto;
import dev.thural.shopping_cart.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    public CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void addItemToCart(CartDto cart, Product product, HttpSession session) {
        CartItemDto existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            // Product already exists in the cart, increment the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            // Product is not in the cart, create a new CartItem
            CartItemDto cartItem = new CartItemDto();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cart.getItems().add(cartItem);
        }
    }
}