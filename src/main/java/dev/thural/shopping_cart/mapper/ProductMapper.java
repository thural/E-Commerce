package dev.thural.shopping_cart.mapper;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.ProductDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }
}