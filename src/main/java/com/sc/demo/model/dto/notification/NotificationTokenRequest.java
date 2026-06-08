package com.sc.demo.model.dto.notification;

public record NotificationTokenRequest(
        String token,
        Long userId
) {
}
