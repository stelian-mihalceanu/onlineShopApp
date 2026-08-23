package com.onlinestore.service;

import com.onlinestore.event.CartEvent;
import com.onlinestore.model.CartItem;
import com.onlinestore.model.Product;
import com.onlinestore.repository.CartRepository;
import com.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartEventProducer cartEventProducer;

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       CartEventProducer cartEventProducer) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartEventProducer = cartEventProducer;
    }

    public List<CartItem> getCartItemsByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(String userId, String productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        Product product = productOpt.get();

        Optional<CartItem> existingItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            CartItem updated = cartRepository.save(existingItem);

            CartEvent event = new CartEvent(userId, productId, updated.getQuantity(), "UPDATE");
            cartEventProducer.sendCartEvent(event);

            return updated;
        } else {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setProductName(product.getName());
            newItem.setProductPrice(product.getPrice());
            newItem.setQuantity(quantity);
            CartItem saved = cartRepository.save(newItem);

            CartEvent event = new CartEvent(userId, productId, quantity, "ADD");
            cartEventProducer.sendCartEvent(event);

            return saved;
        }
    }

    public void removeItem(String userId, String productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);

        CartEvent event = new CartEvent(userId, productId, 0, "REMOVE");
        cartEventProducer.sendCartEvent(event);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
