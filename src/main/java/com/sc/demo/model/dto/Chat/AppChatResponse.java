package com.sc.demo.model.dto.Chat;

import java.time.LocalDateTime;

public record AppChatResponse(
        Long userId,
        String chatTitle,
        String chatDescription,
        LocalDateTime createDate
) {
}
