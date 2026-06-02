package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record appChatResponse(
        Long chatId,
        String chatTitle,
        String messages,
        LocalDateTime createDate
) {
}
