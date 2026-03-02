package com.anusha.notification_engine.controller;

import com.anusha.notification_engine.dto.NotificationRequest;
import com.anusha.notification_engine.model.Notification;
import com.anusha.notification_engine.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/process")
    public Notification process(@RequestBody NotificationRequest request) {

        return service.processNotification(
                request.getUserId(),
                request.getMessage(),
                request.getContext()
        );
    }
}