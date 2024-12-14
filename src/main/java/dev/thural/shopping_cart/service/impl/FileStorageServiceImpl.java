package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.exception.FileStorageException;
import dev.thural.shopping_cart.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    // Allowed file extensions
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );
    // Maximum file size (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Store a single file with validation and unique naming
     *
     * @param file MultipartFile to be stored
     * @return Stored filename
     */
    public String storeFile(MultipartFile file) {
        // Validate file
        validateFile(file);

        // Normalize filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);

        // Generate unique filename
        String fileName = UUID.randomUUID() + "." + fileExtension;

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Store multiple files
     *
     * @param files Array of MultipartFiles to be stored
     * @return List of stored filenames
     */
    public List<String> storeMultipleFiles(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::storeFile)
                .collect(Collectors.toList());
    }

    /**
     * Load file as Path
     *
     * @param filename Name of the file to load
     * @return Path to the file
     */
    public Path loadFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                throw new FileStorageException("File not found " + filename);
            }

            return filePath;
        } catch (Exception ex) {
            throw new FileStorageException("File not found " + filename, ex);
        }
    }

    /**
     * Delete a file
     *
     * @param filename Name of the file to delete
     */
    public void deleteFile(String filename) {
        try {
            Path filePath = loadFile(filename);
            Files.delete(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + filename, ex);
        }
    }

    /**
     * Validate file before storage
     *
     * @param file MultipartFile to validate
     */
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot store empty file");
        }

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("File is too large. Maximum size is 5MB");
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new FileStorageException("Invalid file type. Allowed types: " +
                    String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    /**
     * Extract file extension from filename
     *
     * @param filename Original filename
     * @return File extension
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new FileStorageException("File must have an extension");
        }
        return filename.substring(dotIndex + 1);
    }

    /**
     * Get upload directory path
     *
     * @return Path of upload directory
     */
    public Path getUploadDirectoryPath() {
        return this.fileStorageLocation;
    }
}