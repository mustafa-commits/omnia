package com.sc.demo.model.dto.Notification;

public record NotificationResponse(
        long notification_details_id,
        long user_id,
        long notification
) {
}
