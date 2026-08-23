package com.onlinestore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Optional convenience fields (denormalized) if you want to access name/price easily
    @Transient
    public String getProductName() {
        return product != null ? product.getName() : null;
    }

    @Transient
    public Double getProductPrice() {
        return product != null ? product.getPrice() : null;
    }
}
