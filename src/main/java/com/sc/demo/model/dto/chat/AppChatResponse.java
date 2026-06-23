package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record AppChatResponse(
        Long chatId,
        String chatTitle,
        String messages,
        LocalDateTime createDate,
        Long msgActive,
        Long msgActivity
) {
}
