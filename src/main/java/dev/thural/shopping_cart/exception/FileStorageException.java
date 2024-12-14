/**
 * Exception for file storage related errors
 */
package dev.thural.shopping_cart.exception;

public class FileStorageException extends ApplicationException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
