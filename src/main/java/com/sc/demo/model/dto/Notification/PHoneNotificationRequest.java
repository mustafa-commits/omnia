package com.sc.demo.model.dto.Notification;
import java.time.LocalDateTime;

public record PHoneNotificationRequest(
        LocalDateTime createDate,
        String title,
        String description
) {
}
