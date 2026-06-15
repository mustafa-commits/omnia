package com.sc.demo.model.dto.announcements;

public record AnnouncementsTokenRequest(
        String token,
        Long userId
) {
}
