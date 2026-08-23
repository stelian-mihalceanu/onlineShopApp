package com.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class OnlinestoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinestoreApplication.class, args);
    }
}
