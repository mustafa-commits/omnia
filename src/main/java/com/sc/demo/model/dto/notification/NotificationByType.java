package com.sc.demo.model.dto.notification;

import java.time.LocalDateTime;

public record NotificationByType(
        Long notificationId,
        LocalDateTime createDate,
        String title,
        String description,
        Long sendingType,
        Long userId
) {
}
