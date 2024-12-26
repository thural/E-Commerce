package dev.thural.shopping_cart.mapper;

import dev.thural.shopping_cart.entity.CartItem;
import dev.thural.shopping_cart.model.CartItemDto;
import dev.thural.shopping_cart.model.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemMapper {

    private final ProductMapper productMapper;

    CartItemDto toDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        BeanUtils.copyProperties(cartItem, dto);
        ProductDto productDto = productMapper.toDto(cartItem.getProduct());
        dto.setProduct(productDto);
        return dto;
    }
}
