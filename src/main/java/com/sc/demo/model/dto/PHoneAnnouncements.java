package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record PHoneAnnouncements(
        Long user_id,
        String title,
        String description,
        LocalDateTime createDate,
        String fileName
) {
}
