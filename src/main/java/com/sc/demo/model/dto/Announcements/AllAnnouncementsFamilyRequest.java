package com.sc.demo.model.dto.Announcements;

import java.time.LocalDateTime;

public record AllAnnouncementsFamilyRequest(
        LocalDateTime createDate,
        String title,
        String description
) {
}
