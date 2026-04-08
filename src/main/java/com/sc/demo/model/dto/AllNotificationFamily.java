package com.sc.demo.model.dto;

import com.sc.demo.model.notification.NotificationType;
import java.time.LocalDateTime;

public record AllNotificationFamily(
        LocalDateTime createDate,
        String title,
        String description,
        NotificationType NotificationType/*,
        Long user_id*/
) {
}
