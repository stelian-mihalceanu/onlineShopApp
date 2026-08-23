package com.onlinestore.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CartEventConsumer {

    @KafkaListener(topics = "cart-events", groupId = "onlinestore-group")
    public void listen(String message) {
        // For now just log; later you can add analytics, notifications, etc.
        System.out.println("Received cart event: " + message);
    }
}
