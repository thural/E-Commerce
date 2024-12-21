package dev.thural.shopping_cart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.thural.shopping_cart.entity.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto extends BaseResponse {
    private Product product;
    private Integer quantity;
}