package com.sc.demo.model.dto.announcements;

import java.time.LocalDateTime;

public record AllAnnouncementsFamilyRequest(
        Long announcementsId,
        LocalDateTime createDate,
        String title,
        String fileName,
        String description,
        Long sendingType
) {
}
