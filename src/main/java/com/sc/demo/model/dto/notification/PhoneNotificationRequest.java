package com.sc.demo.model.dto.notification;
import java.time.LocalDateTime;

public record PhoneNotificationRequest(
        LocalDateTime createDate,
        String title,
        String description
) {
}
