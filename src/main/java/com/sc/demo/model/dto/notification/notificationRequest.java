package com.sc.demo.model.dto.notification;

import com.sc.demo.model.notification.notificationDetails;
import com.sc.demo.model.notification.notificationToken;
import com.sc.demo.model.notification.notificationType;

import java.util.List;

public record notificationRequest(
        Integer sendId,
        String title,
        String description,
        notificationType notificationType,
        List<notificationDetails> notificationDetails,
        List<notificationToken> notificationTokens
) {
}
