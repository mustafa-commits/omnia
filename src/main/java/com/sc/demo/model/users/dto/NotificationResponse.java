package com.sc.demo.model.users.dto;

public record NotificationResponse(
        long notification_details_id,
        long user_id,
        long notification
) {
}
