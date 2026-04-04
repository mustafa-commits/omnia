package com.sc.demo.model.users.dto;

import com.sc.demo.model.notification.NotificationDetails;

import java.util.List;

public record NotificationRequest(
        Integer sendId,
        String title,
        String description,
        List<NotificationDetails> notificationDetails
) {
}
