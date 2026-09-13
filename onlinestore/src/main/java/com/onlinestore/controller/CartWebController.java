package com.onlinestore.controller;

import com.onlinestore.model.CartItem;
import com.onlinestore.service.CartService;
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
    public String viewCart(Principal principal, Model model) {
        List<CartItem> cartItems = cartService.getCart(principal.getName());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Principal principal) {
        cartService.addToCart(principal.getName(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, Principal principal) {
        cartService.removeItem(principal.getName(), id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return "redirect:/cart";
    }
}
