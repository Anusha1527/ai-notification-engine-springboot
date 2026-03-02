package com.anusha.notification_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.anusha.notification_engine")
public class NotificationEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationEngineApplication.class, args);
    }
}