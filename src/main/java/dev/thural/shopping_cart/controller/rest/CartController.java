package dev.thural.shopping_cart.controller.rest;

import dev.thural.shopping_cart.mapper.CartMapper;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.request.CartRequest;
import dev.thural.shopping_cart.service.CartItemService;
import dev.thural.shopping_cart.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;


    @PostMapping("/handleAction")
    public ResponseEntity<CartDto> handleCartAction(
            @RequestBody CartRequest request,
            HttpSession session
    ) {
        CartDto dto = cartService.handleCartAction(session, request);
        return ResponseEntity.ok(dto);
    }
}
