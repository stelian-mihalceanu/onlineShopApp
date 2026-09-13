package com.onlinestore.controller;

import com.onlinestore.dto.OrderResponse;
import com.onlinestore.model.Order;
import com.onlinestore.security.JwtUtil;
import com.onlinestore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    private String username(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Bearer token is required");
        }
        return jwtUtil.extractUsername(authHeader.substring(7));
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@RequestHeader("Authorization") String authHeader) {
        return OrderResponse.from(orderService.checkout(username(authHeader)));
    }

    @GetMapping
    public List<OrderResponse> orders(@RequestHeader("Authorization") String authHeader) {
        return orderService.getOrders(username(authHeader)).stream().map(OrderResponse::from).toList();
    }
}
