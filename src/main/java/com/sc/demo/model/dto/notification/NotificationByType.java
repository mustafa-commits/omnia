package com.sc.demo.model.dto.notification;

import java.time.LocalDate;

public record NotificationByType(
        Integer sendId,
        LocalDate createDate,
        String title,
        String description,
        String UserListing
) {
}
