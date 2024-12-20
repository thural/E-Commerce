package dev.thural.shopping_cart.util.annotation;

import dev.thural.shopping_cart.util.validator.FileSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileSizeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileSize {
    String message() default "File size exceeds limit";

    long maxSize() default 5 * 1024 * 1024; // Default is 5 MB

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
