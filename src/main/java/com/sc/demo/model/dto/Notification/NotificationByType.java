package com.sc.demo.model.dto.Notification;

import java.time.LocalDateTime;

public record NotificationByType(
        Integer sendId,
        LocalDateTime createDate,
        String title,
        String description,
        String UserListing
) {
}
