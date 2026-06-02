package com.sc.demo.model.dto.notification;

import java.time.LocalDateTime;

public record notificationByType(
        Integer sendId,
        LocalDateTime createDate,
        String title,
        String description,
        String UserListing
) {
}
