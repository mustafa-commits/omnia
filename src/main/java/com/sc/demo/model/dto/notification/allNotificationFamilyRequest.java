package com.sc.demo.model.dto.notification;

import com.sc.demo.model.notification.notificationType;
import java.time.LocalDateTime;

public record allNotificationFamilyRequest(
        LocalDateTime createDate,
        String title,
        String description,
        notificationType NotificationType
) {
}
