package com.onlinestore.dto;

import com.onlinestore.model.OrderItem;

public record OrderItemResponse(
        Long productId,
        String productName,
        int quantity,
        double unitPrice,
        double lineTotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
    }
}
