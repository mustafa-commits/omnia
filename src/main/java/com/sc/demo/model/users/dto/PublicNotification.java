package com.sc.demo.model.users.dto;
import java.time.LocalDateTime;

public record PublicNotification(
        LocalDateTime createDate,
        String title,
        String description,
        Long user_id
) {
}
