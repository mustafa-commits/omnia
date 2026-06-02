package com.sc.demo.model.dto.announcements;

import com.sc.demo.model.announcements.announcementsDetails;
import java.util.List;

public record announcementsRequest(
        Integer sendId,
        String title,
        String description,
        List<announcementsDetails> announcementsDetails
) {
}
