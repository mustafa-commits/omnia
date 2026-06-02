package com.sc.demo.model.dto.notification;
import java.time.LocalDateTime;

public record phoneNotificationRequest(
        LocalDateTime createDate,
        String title,
        String description
) {
}
