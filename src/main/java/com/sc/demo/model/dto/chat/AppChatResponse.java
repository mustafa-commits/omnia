package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record AppChatResponse(
        String chatTitle,
        String messages,
        LocalDateTime createDate
) {
}
