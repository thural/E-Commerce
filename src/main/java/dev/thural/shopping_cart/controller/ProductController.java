package dev.thural.shopping_cart.controller;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.mapper.ProductMapper;
import dev.thural.shopping_cart.model.CartDto;
import dev.thural.shopping_cart.model.ProductDto;
import dev.thural.shopping_cart.service.CartService;
import dev.thural.shopping_cart.service.FileStorageService;
import dev.thural.shopping_cart.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final CartService cartService;
    private final ProductMapper productMapper;
    private final ProductService productService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String listProducts(Model model, HttpSession session) {
        List<ProductDto> products = productService.getAll().stream()
                .map(productMapper::toDto)
                .toList();
        CartDto cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        log.info("products on getAll: {}", products);
        return "products/index";
    }

    @GetMapping("/productDetails/{productId}")
    public String showProductDetails(@PathVariable Long productId, Model model, HttpSession session) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        CartDto cart = cartService.getCart(session);
        model.addAttribute("product", product);
        model.addAttribute("cart", cart);
        return "productDetails";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        CartDto cart = cartService.getCart(session);
        cartService.addItemToCart(cart, product, session);
        return "redirect:/products";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", new ProductDto());
        }
        if (!model.containsAttribute("categories")) {
            // TODO: move category data to another layer
            List<String> categories = List.of("PHONE", "COMPUTER", "ACCESSORY", "PRINTER", "CAMERA", "OTHER");
            model.addAttribute("categories", categories);
        }
        return "products/create-product-form";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // Validate form
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDto", bindingResult);
            redirectAttributes.addFlashAttribute("productDto", productDto);
            return "redirect:/products/create";
        }

        try {
            // Store file and save product
            String imageFileName = fileStorageService.storeFile(productDto.getImageFile());
            productDto.setImageFileName(imageFileName);
            productService.saveProduct(productDto);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully");
            return "redirect:/products";

        } catch (Exception e) {
            log.error("Product creation failed", e);
            bindingResult.reject("product.creation.error", "Failed to create product");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDto", bindingResult);
            redirectAttributes.addFlashAttribute("productDto", productDto);
            return "redirect:/products/create";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        return productService.getProductById(id)
                .map(product -> {
                    // Prepare DTO for form
                    ProductDto productDto = new ProductDto();
                    BeanUtils.copyProperties(product, productDto);

                    // Add attributes
                    model.addAttribute("product", product);
                    model.addAttribute("productDto", productDto);

                    return "products/edit-product";
                })
                .orElseGet(() -> {
                    // Handle product not found
                    redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
                    return "redirect:/products";
                });
    }

    @PostMapping("/edit")
    public String updateProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult,
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {
        // Validate form
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productDto", bindingResult);
            redirectAttributes.addFlashAttribute("productDto", productDto);
            return "redirect:/products/edit?id=" + id;
        }

        try {
            // Update product
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
                return "redirect:/products";
            }

            // Handle file upload if new image provided
            if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
                String imageFileName = fileStorageService.storeFile(productDto.getImageFile());
                productDto.setImageFileName(imageFileName);
            }

            productService.updateProduct(productDto, existingProduct.get());

            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully");
            return "redirect:/products";

        } catch (Exception e) {
            log.error("Product update failed", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update product");
            return "redirect:/products/edit?id=" + id;
        }
    }

    @GetMapping("/delete")
    public String deleteProduct(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully");
        } catch (Exception e) {
            log.error("Product deletion failed", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product");
        }
        return "redirect:/products";
    }
}