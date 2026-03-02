package com.anusha.notification_engine.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String userId;
    private String message;
    private String context;
    private String decision;
    private String reason;
    private long timestamp;
}