/**
 * Exception for validation errors
 */
package dev.thural.shopping_cart.exception;

public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}