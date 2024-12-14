/**
 * Global exception handler for the application
 */
package dev.thural.shopping_cart.exception;

import dev.thural.shopping_cart.exception.FileStorageException;
import dev.thural.shopping_cart.exception.ResourceNotFoundException;
import dev.thural.shopping_cart.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle file storage exceptions
     */
    @ExceptionHandler(FileStorageException.class)
    public String handleFileStorageException(FileStorageException ex, RedirectAttributes redirectAttributes) {
        log.error("File Storage Error: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage",
                "File upload failed: " + ex.getMessage());
        return "redirect:/error";
    }

    /**
     * Handle resource not found exceptions
     */
    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(Exception ex, Model model) {
        log.error("Resource Not Found: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler({
            ValidationException.class,
            ConstraintViolationException.class
    })
    public String handleValidationException(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Validation Error: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("validationErrors", ex.getMessage());
        return "redirect:/error";
    }

    /**
     * Handle file upload size exceeded
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        log.error("File size exceeded: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage",
                "File is too large. Maximum file size is 5MB");
        return "redirect:/error";
    }

    /**
     * Catch-all handler for unexpected errors
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        log.error("Unexpected Error: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error occurred");
        return "error/500";
    }
}