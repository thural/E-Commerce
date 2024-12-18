package dev.thural.shopping_cart.controller;

import dev.thural.shopping_cart.model.RegistrationDto;
import dev.thural.shopping_cart.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return "redirect:/login?logout";
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
        try {
            log.info("registering user ...");
            authService.registerNewUser(dto);
            return "redirect:/register?success";
        } catch (Exception e) {
            log.info("Registration could not be completed: {}", e.getMessage());
            bindingResult.reject(
                    "error.registrationFailed",
                    "Registration could not be completed"
            );
            return "register";
        }
    }
}
