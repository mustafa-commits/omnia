package com.sc.demo.model.dto.announcements;

import java.time.LocalDateTime;

public record allAnnouncementsFamilyRequest(
        LocalDateTime createDate,
        String title,
        String fileName,
        String description
) {
}
