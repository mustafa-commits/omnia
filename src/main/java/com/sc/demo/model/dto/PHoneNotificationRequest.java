package com.sc.demo.model.dto;
import java.time.LocalDateTime;

public record PHoneNotificationRequest(
        LocalDateTime createDate,
        String title,
        String description/*,
        Long user_id*/
) {
}
