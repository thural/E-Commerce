package dev.thural.shopping_cart.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank(message = "product name required")
    private String name;
    @NotBlank(message = "product brand required")
    private String brand;
    @NotBlank(message = "product category required")
    private String category;

    @Min(value = 0, message = "invalid price value")
    private Double price;

    @Size(min = 1, max = 900, message = "at least 1 and max 900 characters expected")
    private String description;

    private MultipartFile imageFile;

}
