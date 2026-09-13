package com.onlinestore.dto;

import com.onlinestore.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        double totalAmount,
        String status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
