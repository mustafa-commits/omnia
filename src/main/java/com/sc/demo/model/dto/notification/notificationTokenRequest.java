package com.sc.demo.model.dto.notification;

public record notificationTokenRequest(
        String token,
        Long userId
) {
}
