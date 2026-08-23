package com.onlinestore.repository;

import com.onlinestore.model.CartItem;
import com.onlinestore.model.Product;
import com.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}