package com.anusha.notification_engine.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private String userId;
    private String message;
    private String context;
}