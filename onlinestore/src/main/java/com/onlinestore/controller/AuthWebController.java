package com.onlinestore.controller;

import org.springframework.security.core.Authentication;
import com.onlinestore.model.User;
import com.onlinestore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthWebController {

    private final UserService userService;

    public AuthWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        // If already logged in, redirect to home or products
        return "login"; // resolves to templates/login.html
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // resolves to templates/register.html
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        return "redirect:/login?registered";
    }
}
