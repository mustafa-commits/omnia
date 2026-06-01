package com.sc.demo.model.dto.notification;

public record NotificationTokenRequest(
        String Token,
        Long userId
) {
}
