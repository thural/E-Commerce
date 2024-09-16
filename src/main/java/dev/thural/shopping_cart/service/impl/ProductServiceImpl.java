package dev.thural.shopping_cart.service.impl;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.ProductDto;
import dev.thural.shopping_cart.repository.ProductRepository;
import dev.thural.shopping_cart.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    private String saveImage(MultipartFile image) {
        OffsetDateTime time = OffsetDateTime.now();
        String filename = time + "_" + image.getOriginalFilename();
        String uploadDir = "public/images";
        Path uploadPath = Paths.get(uploadDir);

        try (InputStream inputStream = image.getInputStream()) {
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Files.copy(inputStream, Paths.get(uploadDir, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.info("failed to save product image due to: {}", e.getMessage());
        }
        return filename;
    }

    private void deleteImage(Product product) {
        try {
            String uploadDir = "public/images";
            Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
            Files.delete(oldImagePath);
        } catch (Exception e) {
            log.info("failed to update product due to: {}", e.getMessage());
        }
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        String filename = saveImage(productDto.getImageFile());
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        product.setImageFileName(filename);
        repository.save(product);
    }

    @Override
    public ProductDto getProductDtoById(Long id) {
        Product product = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void updateProduct(ProductDto productDto, Product product) {
        if (!productDto.getImageFile().isEmpty()) deleteImage(product);
        String filename = saveImage(productDto.getImageFile());
        BeanUtils.copyProperties(productDto, product);
        product.setImageFileName(filename);
        repository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        repository.findById(id).ifPresentOrElse(
                product -> {
                    deleteImage(product);
                    repository.delete(product);
                },
                () -> log.info("requested product does not exist")
        );
    }
}
