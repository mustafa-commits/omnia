package com.sc.demo.model.dto.announcements;

import java.time.LocalDateTime;

public record PhoneAnnouncementsRequest(
        String title,
        String description,
        LocalDateTime createDate,
        String fileName,
        Long pin
) {
}
