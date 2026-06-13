package com.sc.demo.model.dto.announcements;

import java.time.LocalDate;

public record AllAnnouncementsFamilyRequest(
        LocalDate createDate,
        String title,
        String fileName,
        String description
) {
}
