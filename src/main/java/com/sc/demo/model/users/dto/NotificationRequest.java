package com.sc.demo.model.users.dto;

public record NotificationRequest(
        Integer receiveId,
        Integer sendId,
        String title,
        String description
) {
}
