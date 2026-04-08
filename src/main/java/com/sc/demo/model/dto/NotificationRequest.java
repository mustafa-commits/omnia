package com.sc.demo.model.dto;

import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.notification.NotificationType;

import java.util.List;

public record NotificationRequest(
        Integer sendId,
        String title,
        String description,
        NotificationType notificationType,
        List<NotificationDetails> notificationDetails
) {
}
