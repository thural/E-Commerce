package dev.thural.shopping_cart.controller.rest;

import dev.thural.shopping_cart.mapper.CartMapper;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.service.CartItemService;
import dev.thural.shopping_cart.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;

    @PostMapping("/addToCart/{productId}")
    public ResponseEntity<CartDto> addToCart(
            @PathVariable Long productId,
            HttpSession session
    ) {
        CartDto dto = cartService.addItemToCartById(session, productId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/removeFromCart/{cartItemId}")
    public ResponseEntity<CartDto> removeFromCart(
            @PathVariable Long cartItemId,
            HttpSession session
    ) {
        CartDto updatedCart = cartService.removeItemFromCartById(session, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }
}
