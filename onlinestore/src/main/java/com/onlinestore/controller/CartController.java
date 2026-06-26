package com.onlinestore.controller;

import com.onlinestore.model.CartItem;
import com.onlinestore.security.JwtUtil;
import com.onlinestore.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    public CartController(CartService cartService, JwtUtil jwtUtil) {
        this.cartService = cartService;
        this.jwtUtil = jwtUtil;
    }

    private String extractUsername(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUsername(token);
    }

    @PostMapping("/add")
    public CartItem addToCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        String username = extractUsername(authHeader);
        return cartService.addToCart(username, productId, quantity);
    }

    @GetMapping
    public List<CartItem> getCart(@RequestHeader("Authorization") String authHeader) {
        String username = extractUsername(authHeader);
        return cartService.getCart(username);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestHeader("Authorization") String authHeader) {
        String username = extractUsername(authHeader);
        cartService.clearCart(username);
    }
}
