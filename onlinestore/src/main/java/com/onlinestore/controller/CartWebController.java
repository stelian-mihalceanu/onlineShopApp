package com.onlinestore.controller;

import com.onlinestore.model.CartItem;
import com.onlinestore.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

import java.util.List;

@Controller
public class CartWebController {

    private final CartService cartService;

    public CartWebController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        String username = authentication.getName();

        List<CartItem> cartItems = cartService.getCart(username);

        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            Principal principal
    ) {
        cartService.removeItem(principal.getName(), id);
        return "redirect:/cart";
    }
}