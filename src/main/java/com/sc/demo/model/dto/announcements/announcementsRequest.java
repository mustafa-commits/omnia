package com.sc.demo.model.dto.announcements;

import com.sc.demo.model.announcements.AnnouncementsDetails;
import java.util.List;

public record announcementsRequest(
        Integer sendId,
        String title,
        String description,
        List<AnnouncementsDetails> announcementsDetails
) {
}
