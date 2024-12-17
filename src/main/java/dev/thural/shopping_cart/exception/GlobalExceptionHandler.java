/**
 * Global exception handler for the application
 */
package dev.thural.shopping_cart.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
//    @ExceptionHandler({
//            ValidationException.class,
//            ConstraintViolationException.class
//    })
//    public String handleValidationException(Exception ex, RedirectAttributes redirectAttributes) {
//        log.error("Validation Error: {}", ex.getMessage(), ex);
//        redirectAttributes.addFlashAttribute("validationErrors", ex.getMessage());
//        return "redirect:/error";
//    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Optional.ofNullable(error.getDefaultMessage()).orElse("Invalid input")
                ));

        log.warn("Validation errors occurred: {}", errors);

        redirectAttributes.addFlashAttribute("validationErrors", errors);
        String referrer = request.getHeader("Referer");
        if (referrer == null || referrer.isEmpty()) return "redirect:/";

        try {
            URL url = new URL(referrer);
            String path = url.getPath();
            if (path == null || path.isEmpty() || path.equals("/")) return "redirect:/";
            return "redirect:" + path;
        } catch (MalformedURLException e) {
            log.error("Failed to parse referrer URL", e);
            return "redirect:/";
        }
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            ValidationException.class
    })
    public String handleGenericValidationExceptions(
            Exception ex,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) throws MalformedURLException {
        Map<String, String> errors = new HashMap<>();
        errors.put("globalError", ex.getMessage());

        log.warn("Validation exception occurred: {}", ex.getMessage());

        redirectAttributes.addFlashAttribute("validationErrors", errors);

        String referrer = request.getHeader("Referer");
        return referrer != null && !referrer.isEmpty()
                ? "redirect:" + new URL(referrer).getPath()
                : "redirect:/";
    }
}