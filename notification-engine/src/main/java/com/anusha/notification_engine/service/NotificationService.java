package com.anusha.notification_engine.service;

import com.anusha.notification_engine.ai.AiDecisionService;
import com.anusha.notification_engine.model.Notification;
import com.anusha.notification_engine.repository.NotificationRepository;
import com.anusha.notification_engine.ai.DecisionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final AiDecisionService aiDecisionService;
    public Notification processNotification(String userId,
                                            String message,
                                            String context) {

        DecisionResult aiResult =
        aiDecisionService.analyze(message);

Notification notification = Notification.builder()
        .userId(userId)
        .message(message)
        .context(context)
        .decision(aiResult.getDecision())
        .reason(aiResult.getReason())
        .timestamp(System.currentTimeMillis())
        .build();

return repository.save(notification);
    }
}