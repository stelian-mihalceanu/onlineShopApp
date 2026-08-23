package com.onlinestore.service;

import com.onlinestore.event.CartEvent;
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
    private final CartEventProducer cartEventProducer;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       CartEventProducer cartEventProducer) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartEventProducer = cartEventProducer;
    }

    public List<CartItem> getCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return cartRepository.findByUser(user);
    }

    public CartItem addToCart(String username, Long productId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Optional<CartItem> existingItemOpt = cartRepository.findByUserAndProduct(user, product);
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            CartItem updated = cartRepository.save(existingItem);

            CartEvent event = new CartEvent(username, String.valueOf(productId), updated.getQuantity(), "UPDATE");
            cartEventProducer.sendCartEvent(event);

            return updated;
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            CartItem saved = cartRepository.save(newItem);

            CartEvent event = new CartEvent(username, String.valueOf(productId), quantity, "ADD");
            cartEventProducer.sendCartEvent(event);

            return saved;
        }
    }

    public void removeItem(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        cartRepository.deleteByUserAndProduct(user, product);

        CartEvent event = new CartEvent(username, String.valueOf(productId), 0, "REMOVE");
        cartEventProducer.sendCartEvent(event);
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        cartRepository.deleteByUser(user);
    }
}
