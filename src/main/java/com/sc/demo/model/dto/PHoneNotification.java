package com.sc.demo.model.dto;
import java.time.LocalDateTime;

public record PHoneNotification(
        LocalDateTime createDate,
        String title,
        String description/*,
        Long user_id*/
) {
}
