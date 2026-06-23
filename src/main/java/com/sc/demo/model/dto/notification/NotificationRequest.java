package com.sc.demo.model.dto.notification;

import com.sc.demo.model.notification.NotificationDetails;
import com.sc.demo.model.notification.SendingType;

import java.util.List;

public record NotificationRequest(
        String title,
        String description,
        SendingType sendingType,
        List<NotificationDetails> notificationDetails
) {
}
