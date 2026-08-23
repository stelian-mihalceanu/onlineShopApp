package com.onlinestore.service;

import com.onlinestore.event.CartEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {

    private static final String TOPIC = "cart-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CartEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCartEvent(CartEvent event) {
        String payload = String.format(
            "{\"userId\":\"%s\",\"productId\":\"%s\",\"quantity\":%d,\"type\":\"%s\"}",
            event.getUserId(),
            event.getProductId(),
            event.getQuantity(),
            event.getType()
        );
        kafkaTemplate.send(TOPIC, event.getUserId(), payload);
    }
}
