package com.onlinestore.controller;

import com.onlinestore.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
