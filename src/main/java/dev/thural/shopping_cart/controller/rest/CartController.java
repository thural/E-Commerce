package dev.thural.shopping_cart.controller.rest;

import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.request.CartRequest;
import dev.thural.shopping_cart.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/handleAction")
    public ResponseEntity<CartDto> handleCartAction(
            @RequestBody CartRequest request,
            HttpSession session
    ) {
        CartDto dto = cartService.handleCartAction(session, request);
        return ResponseEntity.ok(dto);
    }
}
