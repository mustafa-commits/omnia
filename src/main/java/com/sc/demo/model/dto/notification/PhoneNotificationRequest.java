package com.sc.demo.model.dto.notification;
import java.time.LocalDate;

public record PhoneNotificationRequest(
        LocalDate createDate,
        String title,
        String description
) {
}
