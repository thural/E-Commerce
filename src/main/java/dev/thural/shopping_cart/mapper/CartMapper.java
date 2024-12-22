package dev.thural.shopping_cart.mapper;

import dev.thural.shopping_cart.entity.Cart;
import dev.thural.shopping_cart.model.CartDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        BeanUtils.copyProperties(cart, cartDto);
        return cartDto;
    }
}
