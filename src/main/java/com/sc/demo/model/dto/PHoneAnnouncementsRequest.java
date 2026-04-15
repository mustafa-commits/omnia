package com.sc.demo.model.dto;

import java.time.LocalDateTime;

public record PHoneAnnouncementsRequest(
        Long user_id,
        String title,
        String description,
        LocalDateTime createDate,
        String fileName
) {
}
