package com.sc.demo.model.users.dto;

import java.time.LocalDateTime;

public record NotificationByType(
        LocalDateTime createDate,
        String title,
        String description,
        Long user_id
) {
}
