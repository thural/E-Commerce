package dev.thural.shopping_cart.controller;

import dev.thural.shopping_cart.entity.Product;
import dev.thural.shopping_cart.model.ProductDto;
import dev.thural.shopping_cart.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @PostMapping("/edit")
    String editProduct(@Valid @ModelAttribute ProductDto productDto, Model model,
                       BindingResult result, @RequestParam Long id) {
        Product product = service.getProductById(id)
                .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("product", product);
        if (result.hasErrors()) return "products/edit-product-form";
        service.updateProduct(productDto, product);
        return "redirect:/products";
    }

    @GetMapping
    public String showProductList(Model model) {
        List<Product> products = service.getAll();
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/create-product-form";
    }

    @GetMapping("/edit")
    public String getEditPage(Model model, @RequestParam Long id) {
        return service.getProductById(id)
                .map(product -> {
                            model.addAttribute("product", product);
                            ProductDto productDto = new ProductDto();
                            BeanUtils.copyProperties(product, productDto);
                            model.addAttribute("productDto", productDto);
                            return "products/edit-product-page";
                        }
                )
                .orElse("redirect:/products");
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        if (productDto.getImageFile().isEmpty()) result.addError(
                new FieldError("productDto", "imageFile", "image file is required")
        );
        if (result.hasErrors()) return "products/create-product-form";
        service.saveProduct(productDto);
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        service.deleteProductById(id);
        return "redirect:/products";
    }
}
