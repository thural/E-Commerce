package dev.thural.shopping_cart.mapper;

import dev.thural.shopping_cart.entity.Cart;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.CartItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        BeanUtils.copyProperties(cart, cartDto);
        cartDto.setTotalPrice(cart.getTotalItemPrice());
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(cartItemMapper::toDto).toList();
        cartDto.setCartItems(cartItems);
        return cartDto;
    }
}
