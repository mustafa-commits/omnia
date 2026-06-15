package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record DashAppChatResponse(
        String guardianName,
        String chatTitle,
        String messages,
        LocalDateTime createDate
) {
}
