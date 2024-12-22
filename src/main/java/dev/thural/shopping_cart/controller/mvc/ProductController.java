package dev.thural.shopping_cart.controller.mvc;

import dev.thural.shopping_cart.entity.Product;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final CartService cartService;
    private final ProductService productService;
    private final FileStorageService fileStorageService;


    @GetMapping
    public String listProducts(Model model, HttpSession session) {
        List<ProductDto> products = productService.getAllDto();
        CartDto cart = cartService.getCartDto(session);
        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        log.info("products on getAll: {}", products);
        return "products/index";
    }


    @GetMapping("/productDetails/{productId}")
    public String showProductDetails(@PathVariable Long productId, Model model, HttpSession session) {
        Product product = productService.getProductById(productId)
                .orElseThrow(EntityNotFoundException::new);
        CartDto cart = cartService.getCartDto(session);
        model.addAttribute("product", product);
        model.addAttribute("cart", cart);
        return "productDetails";
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
        String imageFileName = fileStorageService.storeFile(productDto.getImageFile());
        productDto.setImageFileName(imageFileName);
        productService.saveProduct(productDto);
        redirectAttributes.addFlashAttribute("successMessage", "Product created successfully");
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        ProductDto productDto = productService.getProductDtoById(id);
        model.addAttribute("productDto", productDto);
        return "products/edit-product";
    }


    @PostMapping("/edit")
    public String updateProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult,
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (productDto.getImageFile() != null && !productDto.getImageFile().isEmpty()) {
            String imageFileName = fileStorageService.storeFile(productDto.getImageFile());
            productDto.setImageFileName(imageFileName);
        }

        productService.updateProduct(productDto, existingProduct);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully");
        return "redirect:/products";
    }


    @GetMapping("/delete")
    public String deleteProduct(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {
        productService.deleteProductById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully");
        return "redirect:/products";
    }


}