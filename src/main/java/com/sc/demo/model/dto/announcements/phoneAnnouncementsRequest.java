package com.sc.demo.model.dto.announcements;

import java.time.LocalDateTime;

public record phoneAnnouncementsRequest(
        String title,
        String description,
        LocalDateTime createDate,
        String fileName
) {
}
