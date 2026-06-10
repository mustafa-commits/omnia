package com.sc.demo.model.dto.announcements;

import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.announcements.Branches;
import com.sc.demo.model.notification.SendingType;

import java.util.List;

public record AnnouncementsRequest(
        Integer sendId,
        String title,
        String description,
        Branches branches,
        SendingType sendingType,
        List<AnnouncementsDetails> announcementsDetails
) {
}
