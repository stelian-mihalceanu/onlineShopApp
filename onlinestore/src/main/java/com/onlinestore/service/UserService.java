package com.onlinestore.service;

import com.onlinestore.model.User;
import com.onlinestore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User user) {
        String username = user.getUsername().trim();
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already registered");
        }

        user.setUsername(username);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        return repo.save(user);
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }
}
