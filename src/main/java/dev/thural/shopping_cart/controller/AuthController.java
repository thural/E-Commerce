package dev.thural.shopping_cart.controller;

import dev.thural.shopping_cart.model.RegistrationDto;
import dev.thural.shopping_cart.service.UserService;
import dev.thural.shopping_cart.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This should correspond to login.html in templates directory
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationDto") RegistrationDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        // Custom validation for password match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "error.passwordMismatch",
                    "Passwords do not match"
            );
        }

        // Check for duplicate email
        if (userService.isDuplicateEmail(dto.getEmail())) {
            bindingResult.rejectValue(
                    "email",
                    "error.duplicateEmail",
                    "Email is already in use"
            );
        }

        // If there are validation errors, return to the form
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(dto);
            return "redirect:/register?success";
        } catch (Exception e) {
            bindingResult.reject(
                    "error.registrationFailed",
                    "Registration could not be completed"
            );
            return "register";
        }
    }
}
