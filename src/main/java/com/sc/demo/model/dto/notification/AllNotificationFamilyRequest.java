package com.sc.demo.model.dto.notification;

import com.sc.demo.model.notification.SendingType;
import java.time.LocalDate;

public record AllNotificationFamilyRequest(
        LocalDate createDate,
        String title,
        String description,
        SendingType SendingType
) {
}
