package com.sc.demo.model.dto.Notification;

public record NotificationTokenRequest(
        String Token,
        Long userId
) {
}
