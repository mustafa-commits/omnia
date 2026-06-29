package com.sc.demo.model.dto.announcements;

import com.sc.demo.model.announcement.AnnouncementsDetails;
import com.sc.demo.model.notification.SendingType;

import java.util.List;

public record AnnouncementsRequest(
        String title,
        String description,
        String branches,
        SendingType sendingType,
        List<AnnouncementsDetails> announcementsDetails
) {
}
