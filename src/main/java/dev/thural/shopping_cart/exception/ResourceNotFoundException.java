/**
 * Exception for resource not found scenarios
 */
package dev.thural.shopping_cart.exception;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
