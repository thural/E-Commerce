package dev.thural.shopping_cart.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {
    /**
     * Store a single file with validation and unique naming
     *
     * @param file MultipartFile to be stored
     * @return Stored filename
     * @throws dev.thural.shopping_cart.exception.FileStorageException if file storage fails
     */
    String storeFile(MultipartFile file);

    /**
     * Store multiple files
     *
     * @param files Array of MultipartFiles to be stored
     * @return List of stored filenames
     * @throws dev.thural.shopping_cart.exception.FileStorageException if file storage fails
     */
    List<String> storeMultipleFiles(MultipartFile[] files);

    /**
     * Load file as Path
     *
     * @param filename Name of the file to load
     * @return Path to the file
     * @throws dev.thural.shopping_cart.exception.FileStorageException if file not found
     */
    Path loadFile(String filename);

    /**
     * Delete a file
     *
     * @param filename Name of the file to delete
     * @throws dev.thural.shopping_cart.exception.FileStorageException if file deletion fails
     */
    void deleteFile(String filename);

    /**
     * Get upload directory path
     *
     * @return Path of upload directory
     */
    Path getUploadDirectoryPath();
}