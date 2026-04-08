package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record PHoneAnnouncements(
        LocalDateTime createDate,
        String title,
        String description
) {
}
