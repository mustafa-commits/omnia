package com.sc.demo.model.dto.chat;

import java.time.LocalDateTime;

public record MessagesResponse(
        String MESSAGES,
        Long RECEIVER,
        Long SENDER,
        LocalDateTime createDate
) {
}
