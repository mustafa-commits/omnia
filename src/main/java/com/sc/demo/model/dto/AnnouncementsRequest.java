package com.sc.demo.model.dto;

import com.sc.demo.model.announcements.AnnouncementsDetails;
import java.util.List;

public record AnnouncementsRequest(
        Integer sendId,
        String title,
        String description,
        List<AnnouncementsDetails> announcementsDetails
) {
}
