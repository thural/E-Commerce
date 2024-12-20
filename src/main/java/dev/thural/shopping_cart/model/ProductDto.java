package dev.thural.shopping_cart.model;

import dev.thural.shopping_cart.util.annotation.ValidFileSize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@Data
public class ProductDto {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "Category is required")
    private String category;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Product image is required")
    @ValidFileSize(maxSize = 5242880, message = "File size must be less than 5MB")
    private MultipartFile imageFile;

    private String imageFileName;
}
