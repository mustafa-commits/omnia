package com.sc.demo.model.dto.notification;

import com.sc.demo.model.notification.SendingType;
import java.time.LocalDateTime;

public record AllNotificationFamilyRequest(
        Long notificationId,
        LocalDateTime createDate,
        String title,
        String description,
        SendingType SendingType
) {
}
