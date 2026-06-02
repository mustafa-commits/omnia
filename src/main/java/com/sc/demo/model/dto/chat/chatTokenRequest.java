package com.sc.demo.model.dto.chat;

public record chatTokenRequest(
        String token,
        Long chatId
) {
}
