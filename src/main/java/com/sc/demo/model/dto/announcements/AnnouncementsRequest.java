package com.sc.demo.model.dto.announcements;

import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.notification.SendingType;

import java.util.List;

public record AnnouncementsRequest(
        Long sendId,
        String title,
        String description,
        String branches,
        SendingType sendingType,
        Long createBy,
        List<AnnouncementsDetails> announcementsDetails
) {
}
