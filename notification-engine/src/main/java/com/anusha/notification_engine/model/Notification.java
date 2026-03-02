
package com.anusha.notification_engine.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String message;
    private String context;
    private String decision;
    private String reason;
    private long timestamp;
}