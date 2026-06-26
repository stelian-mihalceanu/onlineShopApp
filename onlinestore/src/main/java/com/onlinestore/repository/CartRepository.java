package com.onlinestore.repository;

import com.onlinestore.model.CartItem;
import com.onlinestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
