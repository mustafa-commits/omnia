package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record DashAppChatResponse(
        Long chatId,
        String guardianName,
        String chatTitle,
        String messages,
        LocalDateTime createDate,
        Long msgActive,
        Long msgActivity
) {
}
