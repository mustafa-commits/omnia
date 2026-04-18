package com.sc.demo.model.dto;

import com.sc.demo.model.notification.NotificationType;
import java.time.LocalDateTime;

public record AllNotificationFamilyRequest(
        LocalDateTime createDate,
        String title,
        String description,
        NotificationType NotificationType
) {
}
