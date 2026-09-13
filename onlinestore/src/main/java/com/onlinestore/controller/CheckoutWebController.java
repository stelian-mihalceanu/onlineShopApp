package com.onlinestore.controller;

import com.onlinestore.model.Order;
import com.onlinestore.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class CheckoutWebController {
    private final OrderService orderService;

    public CheckoutWebController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal) {
        Order order = orderService.checkout(principal.getName());
        return "redirect:/orders/" + order.getId();
    }
}
