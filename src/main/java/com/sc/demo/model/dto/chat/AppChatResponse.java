package com.sc.demo.model.dto.chat;

import java.time.LocalDate;

public record AppChatResponse(
        Long chatId,
        String chatTitle,
        String messages,
        LocalDate createDate
) {
}
