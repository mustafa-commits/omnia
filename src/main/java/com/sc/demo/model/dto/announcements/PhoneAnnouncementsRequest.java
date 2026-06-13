package com.sc.demo.model.dto.announcements;

import java.time.LocalDate;

public record PhoneAnnouncementsRequest(
        String title,
        String description,
        LocalDate createDate,
        String fileName
) {
}
