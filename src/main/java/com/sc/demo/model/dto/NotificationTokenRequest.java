package com.sc.demo.model.dto;

public record NotificationTokenRequest(
        String Token,
        Long userId
) {
}
