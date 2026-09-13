package com.onlinestore.service;

import com.onlinestore.model.CartItem;
import com.onlinestore.model.Product;
import com.onlinestore.model.User;
import com.onlinestore.repository.CartRepository;
import com.onlinestore.repository.ProductRepository;
import com.onlinestore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<CartItem> getCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return cartRepository.findByUser(user);
    }

    public CartItem addToCart(String username, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existing = cartRepository.findByUserAndProduct(user, product);
        int requested = quantity + existing.map(CartItem::getQuantity).orElse(0);
        if (requested > product.getStock()) {
            throw new IllegalStateException("Requested quantity exceeds available stock");
        }

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(requested);
            return cartRepository.save(item);
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        return cartRepository.save(item);
    }

    public void removeItem(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        cartRepository.deleteByUserAndProduct(user, product);
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        cartRepository.deleteByUser(user);
    }
}
