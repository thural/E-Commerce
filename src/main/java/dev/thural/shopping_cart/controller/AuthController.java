package dev.thural.shopping_cart.controller;

import dev.thural.shopping_cart.model.RegistrationDto;
import dev.thural.shopping_cart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String requestRegistration(Model model) {
        var registrationDto = new RegistrationDto();
        model.addAttribute(registrationDto);
        return "authentication/signup";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationDto dto, Model model, BindingResult result) {
        if (!dto.getPassword().equals(dto.getConfirmPassword()))
            result.addError(new FieldError(
                    "registrationDto",
                    "confirmPassword",
                    "passwords mismatch"
            ));

        if (userService.isDuplicateEmail(dto.getEmail()))
            result.addError(new FieldError(
                    "registrationDto",
                    "email",
                    "email is already used"
            ));

        if (!result.hasErrors()) return "authentication/signup";

        try {
            userService.createUser(dto);
            model.addAttribute("registerDto", new RegistrationDto());
            model.addAttribute("success", true);
        } catch (Exception e) {
            result.addError(new FieldError(
                    "registrationDto",
                    "account",
                    e.getMessage()
            ));
        }
        return "authentication/signup";
    }


}
