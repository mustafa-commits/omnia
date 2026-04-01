package com.sc.demo.model.users.dto;

import java.util.List;

public record NotificationRequest(
        Integer receiveId,
        Integer sendId,
        String title,
        String description,
        List notificationDetails
) {
}
