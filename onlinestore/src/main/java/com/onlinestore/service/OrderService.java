package com.onlinestore.service;

import com.onlinestore.model.CartItem;
import com.onlinestore.model.Order;
import com.onlinestore.model.OrderItem;
import com.onlinestore.model.OrderStatus;
import com.onlinestore.model.Product;
import com.onlinestore.model.User;
import com.onlinestore.repository.CartRepository;
import com.onlinestore.repository.OrderRepository;
import com.onlinestore.repository.ProductRepository;
import com.onlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order checkout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<CartItem> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0.0;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() <= 0) throw new IllegalStateException("Cart contains an invalid quantity");
            Product product = productRepository.findByIdForUpdate(cartItem.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product no longer exists"));
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
            total += item.getLineTotal();
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        cartRepository.deleteByUser(user);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderRepository.findByUserWithItems(user);
    }
}
