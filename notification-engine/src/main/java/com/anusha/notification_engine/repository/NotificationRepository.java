package com.anusha.notification_engine.repository;

import com.anusha.notification_engine.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository
        extends MongoRepository<Notification, String> {
}