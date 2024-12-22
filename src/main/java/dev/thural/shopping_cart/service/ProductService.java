package dev.thural.shopping_cart.service;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.ProductDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAll();

    List<ProductDto> getAllDto();

    void saveProduct(ProductDto productDto);

    ProductDto getProductDtoById(Long id);

    Optional<Product> getProductById(Long id);

    void updateProduct(@Valid ProductDto productDto, Product product);

    void deleteProductById(Long id);

}
